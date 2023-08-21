package origin.base.jobs;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author:lmq
 * @Date: 2020/6/9
 * @Desc:
 **/
public class RunJob {
    /**
     * output like this:
     * 0s pool-1-thread-1
     * 5s pool-1-thread-1
     * 10s pool-1-thread-2
     * 15s pool-1-thread-1
     * 20s pool-1-thread-3
     * 25s pool-1-thread-2
     * 30s pool-1-thread-4
     * 35s pool-1-thread-1
     * <p>
     * 结论:
     * 如果task执行时长很长,并不会启动多个线程保障 在period后执行
     * Fixed-rate timers (scheduleAtFixedRate()) are based on the starting time (so each iteration will execute at startTime + iterationNumber * delayTime)
     * Fixed-delay timers (schedule()) are based on the previous execution (so each iteration will execute at lastExecutionTime + delayTime).
     *
     * 使用pool  可以 支持 同时 并行的 任务数量!!!
     * @param args
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis() / 1000;

        //same as timer result TTWWWGGTTWWWTTWWWTT -->  TTWWWGGTTWTTWWWTT  如果gc停顿  会影响后面的 暂停时间 因为scheduleAtFixedRate是要固定rate
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        Runnable runnable = () -> {
            System.out.println(System.currentTimeMillis() / 1000 - start + "s " + Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        };
        Runnable runnable2 = () -> {
            System.out.println(System.currentTimeMillis() / 1000 - start + "s " + Thread.currentThread().getName()+":2");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        };
        Runnable runnable3 = () -> {
            System.out.println(System.currentTimeMillis() / 1000 - start + "s " + Thread.currentThread().getName()+":3");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 2, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(runnable2, 0, 2, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(runnable3, 0, 2, TimeUnit.SECONDS);

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println(System.currentTimeMillis() / 1000 - start + "s " + Thread.currentThread().getName());
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                }
//            }
//        }, new Date(), 2000);

    }
}
