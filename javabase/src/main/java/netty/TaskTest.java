package netty;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author:lmq
 * @Date: 2020/12/12
 * @Desc:
 **/
public class TaskTest {
    private static HashedWheelTimer timer;

    public static void main(String[] args) throws InterruptedException {
        timer = new HashedWheelTimer(new DefaultThreadFactory("redisson-timer"), 100, TimeUnit.MILLISECONDS, 1024, false);
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println(System.currentTimeMillis()+"=>invoke=>"+timeout.toString());
            }
        };
        Timeout timeout = timer.newTimeout(task, 5000, TimeUnit.MILLISECONDS);
        Thread.currentThread().join(100000);
    }
}
