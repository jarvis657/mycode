<?php

namespace App\Console\Commands\Push;

use App\ApplePushEvent;
use App\ApplePushLog;
use App\Commons\Models\AppUser;
use App\Commons\Services\AppleService;
use App\Console\Commands\Command;
use App\Libs\LoggerHelper;
use App\Services\ApplePushService;
use Carbon\Carbon;
use Illuminate\Database\Query\Expression;
use Illuminate\Support\Facades\DB;

/***
 * 每个小时检测一次，当天的执行时间超过18点，则进行发送
 *
 */
class ExecuteNoSubscriptionDevicePushMessage extends Command
{
    protected $signature = 'execute-no-subscription-device-push-message';

    private $maxTries = 3;
    private $waitLongTime = 1800;
//    private $waitLongTime = 5;
    private $waitMinTime = 10;
//    private $waitMinTime = 2;
    private $pushMaxSuccessTimes = 15;
    private $pushMaxTimes = 30;
    protected $isSupervisorProcess = true;

    protected $logItemProcessInfo = true;
    protected $logItemProcessAvgSpeed = true;
    /**
     * @var AppleService
     */
    protected $applePushSvc;

    public function __construct()
    {
        parent::__construct();
        $this->applePushSvc = new ApplePushService();
    }

    public function handle()
    {
        $this->log("{$this->signature} task started");
//        $day = -1;
        while (true) {
            $startNow = now();
//            $day = $day + 1;
//            $startNow = now()->addDays($day);
            $this->log("{$startNow}  check the applePushEvent");
            while (true) {
                try {
                    /**
                     * 1.时间已经超过了 execute_at
                     * 2.当天没有推送记录
                     * 3.没有订阅
                     * 4.推送成功没有到达上限
                     * 5.每天重试3次
                     **/
                    $sql = "select t.*,l.num as day_push_num,l.id as lid
                            from apple_push_events t
                            left join  app_users u on t.device_id = u.id
                            left join apple_push_logs l on t.device_id = l.device_id and l.execute_at > :lcreated_at
                        where t.execute_at < :execat and u.push_token is not null and t.push_success_num < :pushSuccessNum  and  t.push_num < :pushNum and u.subscription_status is null and u.is_apple_review_device <>1
                        and (l.id is null || ( ( l.push_status <> 200 or l.push_status is null ) and l.num < :maxRetries )) limit 100";
                    $ymd_today = $startNow->format('Y-m-d');
                $params = [':execat' => $startNow, ':pushNum' => $this->pushMaxTimes,
                    ':pushSuccessNum' => $this->pushMaxSuccessTimes, ':lcreated_at' => $ymd_today,
                    ':maxRetries' => $this->maxTries
                ];
                $applePushEvents = DB::select($sql, $params);
                    if (empty($applePushEvents) || count($applePushEvents) == 0) {
                        $this->log("没有需要推送的内容，等待10s");
                        break;
                    }
                    $this->log(count($applePushEvents) . "个任务");
                    $content = ["title" => 'Find out what tomorrow holds!', "body" => "Curious about what the AI horoscope has in store for you? Click to receive your personalised horoscope for tomorrow."];
                    foreach ($applePushEvents as $applePushEvent) {
                        $num = $applePushEvent->day_push_num + 1;
                        $push_num = $applePushEvent->push_num + 1;
                        echo "query data: event.day_num " . $num . " push_num: " . $push_num . " eventid: " . $applePushEvent->id . " lid:" . $applePushEvent->lid . "\n";
                        $notificationData = [];
                        $notificationData["app_bundle_id"] = $applePushEvent->app_bundle_id;
                        $notificationData["title"] = $content["title"];
                        $notificationData["body"] = $content["body"];
                        $notificationData["environment"] = $applePushEvent->environment;
                        $device = AppUser::where("uuid", $applePushEvent->device_uuid)->first();
                        $push_token = $device->push_token;
                        $notificationData["push_token"] = $push_token;
                        $applePushEvent->execute_at = $ymd_today . substr($applePushEvent->execute_at, 10);
                        $record = ApplePushLog::updateOrCreate([
                            'device_id' => $applePushEvent->device_id,
                            'user_region' => $applePushEvent->device_time_zone,
                            'user_language' => $applePushEvent->user_language,
                            'app_version' => $applePushEvent->app_version,
                            'environment' => $applePushEvent->environment,
                            'app_bundle_id' => $applePushEvent->app_bundle_id,
                            'app_push_event_id' => $applePushEvent->id,
                            'execute_at' => $applePushEvent->execute_at,
                            'content' => json_encode($content),
                        ], [
                            'num' => $num,
                            'push_date' => now(),
                            'created_at' => now(),
                            'updated_at' => now(),
                        ]);
                        $result = $this->applePushSvc->pushAppleNotificationMessage($notificationData);
                        echo "push_logs: " . $record->id . " exec_at:" . $record->execute_at . " num:" . $record->num . " result:" . $result['error_info'] . "\n";
                        $this->log("推送结果", ["result" => $result]);
                        $record->fill(
                            [
                                'push_status' => $result['code'],
                                'error_info' => $result['error_info'],
                            ])->save();
                        if ($result["code"] == 200) {
                            $successNum = $applePushEvent->push_success_num + 1;
                            DB::update('update apple_push_events set push_num = :pushNum, push_success_num = :successNum,execute_at = :execute_at,last_push_notification_time  = :lastTime where id = :id',
                                [':pushNum' => $push_num, ':successNum' => $successNum, ':id' => $applePushEvent->id, ':execute_at' => $this->getNextDay($applePushEvent->execute_at), ':lastTime' => $ymd_today]);
                        } else {
                            if ($num >= $this->maxTries) {
                                $nextDay = $this->getNextDay($applePushEvent->execute_at);
                                echo '推送次数', $num, '==明天继续', $nextDay;
                                DB::update('update apple_push_events set push_num = :pushNum, execute_at = :execute_at,last_push_notification_time  = :lastTime where id = :id',
                                    [':pushNum' => $push_num, ':id' => $applePushEvent->id, ':execute_at' => $nextDay, ':lastTime' => now()]);
                            } else {
                                DB::update('update apple_push_events set push_num = :pushNum, last_push_notification_time  = :lastTime where id = :id',
                                    [':pushNum' => $push_num, ':id' => $applePushEvent->id, ':lastTime' => now()]);
                            }
                        }
                    }
                } catch (\Exception $e) {
                    $this->log("当前批次处理发生异常", ["error" => $e->getMessage()]);
                    LoggerHelper::serviceError("当前批次处理发生异常", ["error" => $e->getMessage()]);
                } finally {
                    $this->log("处理了一个批次的推送任务，休息10s");
                    sleep($this->waitMinTime);
                }
            }
            $this->log("当前批次处理完毕，半个小时检测....");
            sleep($this->waitLongTime);
        }
    }

    /**成功推送后将执行时间改为下一天**/
    private function getNextDay($execute_at): string
    {
        $execute_at = new Carbon($execute_at);
        $execute_at = $execute_at->addDays(1);
        return $execute_at->format('Y-m-d H:i:s');
    }

}
