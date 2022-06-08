package origin.concurrency.threadlocals;

import ch.qos.logback.classic.util.CopyOnInheritThreadLocal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:lmq
 * @Date: 2022/4/5
 * @Desc:
 **/
public class TestThreadLocal {

    //    private ThreadLocal tl = new ThreadLocal();
    private static InheritableThreadLocal tl = new InheritableThreadLocal(){
        @Override
        protected Object initialValue() {
            return 0;
        }
    };

    //    private static TransmittableThreadLocal tl = new TransmittableThreadLocal();
//    private CopyOnInheritThreadLocal tl = new CopyOnInheritThreadLocal();

    private ExecutorService es = Executors.newFixedThreadPool(120);
//    private Executor es = demoExecutor();


    public static void main(String[] args) {
        TestThreadLocal testThreadLocal = new TestThreadLocal();
        try {
            testThreadLocal.testThreadLocal("10");
            testThreadLocal.testThreadLocal("111");
            testThreadLocal.testThreadLocal("222");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

//    public static Executor demoExecutor() {
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
////        threadPoolTaskExecutor.setTaskDecorator(new GatewayHeaderTaskDecorator());
//        threadPoolTaskExecutor.setCorePoolSize(5);
//        threadPoolTaskExecutor.setQueueCapacity(0);
//        threadPoolTaskExecutor.setKeepAliveSeconds(3600);
//        threadPoolTaskExecutor.setMaxPoolSize(1);
//        threadPoolTaskExecutor.setThreadNamePrefix("demoExecutor-");
//        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//        threadPoolTaskExecutor.initialize();
//        return threadPoolTaskExecutor;
//    }

    public Boolean testThreadLocal(String s) throws Throwable {
        System.out.println("input:" + s);
        HashMap<String,String> map = new HashMap<>();
        map.put(s,s);
//        tl.set(s); // DemoContext为相应的ThreadLocal对象
        tl.set(map); // DemoContext为相应的ThreadLocal对象
        CompletableFuture<Throwable> subThread = CompletableFuture.supplyAsync(() -> {
            try {
                //打印子线程的值
                System.out.println(String.format("子线程id=%s，contextStr为：%s", Thread.currentThread().getId(), tl.get()));
            } catch (Throwable throwable) {
                return throwable;
            }
            return null;
        }); //这里不加线程池 子线程数据有问题？？？很奇怪 待解决 TODO 待深入分析为什么
        Thread.sleep(2000);
        CompletableFuture<Throwable> subThread2 = CompletableFuture.supplyAsync(() -> {
            try {
                //打印子线程的值
                System.out.println(String.format("子线程2id=%s，contextStr为：%s", Thread.currentThread().getId(), tl.get()));
            } catch (Throwable throwable) {
                return throwable;
            }
            return null;
        }); //这里不加线程池 子线程数据有问题？？？很奇怪 待解决 TODO 待深入分析为什么
        //打印主线程的值
        System.out.println(String.format("主线程id=%s，contextStr为：%s", Thread.currentThread().getId(), tl.get()));
        Throwable throwable = subThread.get();
        Throwable throwable2 = subThread2.get();
        if (throwable != null || throwable2 != null) {
            throw throwable;
        }
        tl.remove();
        return true;
    }
}
