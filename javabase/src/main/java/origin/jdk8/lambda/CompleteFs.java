package origin.jdk8.lambda;

import java.sql.Time;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * @Author:lmq
 * @Date: 5055/3/4
 * @Desc:
 **/
public class CompleteFs {

    private static MyThreadExecs myThreadExecs = new MyThreadExecs(2, 2, 1000, TimeUnit.SECONDS,
            new SynchronousQueue(),
            new CallerRunsPolicy());
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
        long start = System.currentTimeMillis();
        CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> {
            try {
//                Thread.sleep(1000 + RandomUtils.nextInt(1000));
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "<商品详情>";
        }, myThreadExecs);

        CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> {
            try {
//                Thread.sleep(3000 + RandomUtils.nextInt(1000));
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "<卖家信息>";
        }, myThreadExecs);

        CompletableFuture<String> futureC = CompletableFuture.supplyAsync(() -> {
            try {
//                Thread.sleep(1000 + RandomUtils.nextInt(1000));
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "<库存信息>";
        }, myThreadExecs);

        CompletableFuture<String> futureD = CompletableFuture.supplyAsync(() -> {
            try {
//                Thread.sleep(1000 + RandomUtils.nextInt(1000));
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "<订单信息>";
        }, myThreadExecs);
        scheduledThreadPoolExecutor.schedule(() -> futureA.cancel(true), 10,
                TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.schedule(() -> futureB.cancel(true), 100,
                TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.schedule(() -> futureC.cancel(true), 100,
                TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.schedule(() -> futureD.cancel(true), 100,
                TimeUnit.MILLISECONDS);

        CompletableFuture<String> combine = futureA.thenCombineAsync(futureB, (a, b) -> {
            return a + b;
        }).thenCombineAsync(futureC, (c, d) -> {
            return c + d;
        }).thenCombineAsync(futureD, (e, f) -> {
            return e + f;
//        }).applyToEitherAsync(timeoutAfter(1, TimeUnit.SECONDS), a -> a).get();
        }).handle((d, e) -> {
            if (e != null) {
                throw new RuntimeException("已被cancel",e);
            }
            return d;
        });
//        }).acceptEitherAsync(timeoutAfter(10, TimeUnit.SECONDS), System.out::println);
        System.out.println(combine.get());
        System.out.println("done");

//        try {
//            long start5 = System.currentTimeMillis();
//            String s = timeoutAfter(futureA, 5, TimeUnit.SECONDS).thenCombineAsync(
//                    timeoutAfter(futureC, 5, TimeUnit.SECONDS), (a, b) -> {
//                        System.out.println(a + b);
//                        return a + b;
//                    }).thenCombineAsync(timeoutAfter(futureD, 5, TimeUnit.SECONDS), (c, d) -> {
//                System.out.println(c + d);
//                return c + d;
//            }).thenCombineAsync(timeoutAfter(futureB, 5, TimeUnit.SECONDS), (e, f) -> {
//                System.out.println(e + f);
//                return e + f;
//            }).get(1000, TimeUnit.SECONDS);
//            System.out.println(s);
//            System.out.println(System.currentTimeMillis() - start5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }

//        CompletableFuture<Void> allFuture = CompletableFuture.allOf(futureA, futureC, futureB, futureD);
//        allFuture.join();
//        System.out.println("all:" + futureA.get() + futureB.get() + futureC.get() + futureD.get());
//        System.out.println("总耗时:" + (System.currentTimeMillis() - start));
//        myThreadExecs.shutdown();

        //jdk9: orTimeout  completeOnTimeout
//        futureA.thenCombine(futureB, (a, b) -> a + b)
//                .orTimeout(1, TimeUnit.SECONDS)
//                .whenComplete((amount, error) -> {
//                    if (error == null) {
//                        System.out.println("The price is: " + amount + "GBP");
//                    } else {
//                        System.out.println("Sorry, we could not return you a result");
//                    }
//                });
//        String DEFAULT_V = "vv";
//        futureA.thenCombine(futureB, (a, b) -> a + b)
//                .completeOnTimeout(DEFAULT_V, 1, TimeUnit.SECONDS)
//                .whenComplete((amount, error) -> {
//                    if (error == null) {
//                        System.out.println("The price is: " + amount + "GBP");
//                    } else {
//                        System.out.println("Sorry, we could not return you a result");
//                    }
//                });

        myThreadExecs.shutdown();
        scheduledThreadPoolExecutor.shutdown();
    }

    public static <T> CompletableFuture<T> timeoutAfter(long timeout, TimeUnit unit) {
        CompletableFuture f = new CompletableFuture();
        scheduledThreadPoolExecutor.schedule(() -> f.completeExceptionally(new TimeoutException()), timeout, unit);
        return f;
    }

    public static <T> CompletableFuture<T> timeoutAfter(CompletableFuture<T> f, long timeout, TimeUnit unit) {
        scheduledThreadPoolExecutor.schedule(() -> f.completeExceptionally(new TimeoutException()), timeout, unit);
        return f;
    }
}
