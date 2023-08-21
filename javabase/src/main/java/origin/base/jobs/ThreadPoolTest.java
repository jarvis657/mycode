package origin.base.jobs;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author:lmq
 * @Date: 2020/10/20
 * @Desc:
 **/
public class ThreadPoolTest {
    public static void main(String[] args) {
        AtomicLong atomicLong = new AtomicLong();
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis() / 1000 + " A...." + atomicLong.getAndAdd(1));
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, 1, TimeUnit.SECONDS);

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis() / 1000 + " B...." + atomicLong.getAndAdd(1));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis() / 1000 + " C...." + atomicLong.getAndAdd(1));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);


    }
}
