package origin.base.jobs;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
     * @param args
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis() / 1000;
        Timer timer = new Timer();

        //same as timer result TTWWWGGTTWWWTTWWWTT -->  TTWWWGGTTWTTWWWTT  如果gc停顿  会影响后面的 暂停时间 因为scheduleAtFixedRate是要固定rate
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);
//        Runnable runnable = () -> {
//            System.out.println(System.currentTimeMillis() / 1000 - start + "s " + Thread.currentThread().getName());
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//            }
//        };
//        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 2, TimeUnit.SECONDS);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() / 1000 - start + "s " + Thread.currentThread().getName());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            }
        }, new Date(), 2000);

    }
}
