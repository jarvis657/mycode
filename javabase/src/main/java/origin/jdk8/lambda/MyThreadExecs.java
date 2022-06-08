package origin.jdk8.lambda;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author:lmq
 * @Date: 2022/3/7
 * @Desc:
 **/
public class MyThreadExecs extends ThreadPoolExecutor {

    public MyThreadExecs(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        System.out.printf("after:time:%d:active:%d,completeTask:%d\n", System.currentTimeMillis() / 1000,
                this.getActiveCount(), this.getCompletedTaskCount());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.printf("before:time:%d:active:%d,completeTask:%d\n", System.currentTimeMillis() / 1000,
                this.getActiveCount(), this.getCompletedTaskCount());
    }
}
