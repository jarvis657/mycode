package origin.concurrency.bugs;

//import jdk.internal.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
//import sun.misc.Unsafe;

/**
 * 每n次运行下特定逻辑
 * 程序会有异常, 并发问题
 * 异常问题是 可能是n+1次运行
 * 理想情况下，上述代码会输出1000次”hit”，因为对于一个线程来说，每隔100次循环hit方法会返回true，上述代码会输出一行”hit”，而这里有10个线程，因此应该会输出1000次”hit”。然而，实际情况是，多数情况下，上述代码会输出999次”hit”，偶尔也会输出1000次”hit”，很明显，上述代码出现了并发问题，但看FixedRateSampler的代码我们发现，counter使用了AtomicInteger类，incrementAndGet方法是原子操作；在检查counter的值的时候也使用了『double-check』技巧，似乎并不会出现并发的问题。
 */
public class FixedRateSampler {
//    private static final Unsafe U;
//    private static final long SELFCOUNTER_ADDR;
//    private final static AtomicInteger p = new AtomicInteger();
//    private final static AtomicInteger p_r = new AtomicInteger();
//
//    static {
//        try {
//            U = getUnsafe();
//            Class<?> k = FixedRateSampler.class;
//            SELFCOUNTER_ADDR = U.objectFieldOffset
//                    (k.getDeclaredField("selfCounter"));
//        } catch (Exception e) {
//            throw new Error(e);
//        }
//    }
//
//    private final AtomicInteger counter = new AtomicInteger();
//    private final int rate;
//    private volatile long selfCounter = 0L;
//
//    public FixedRateSampler(int rate) {
//        this.rate = rate;
//    }
//
//    public static void main(String[] args) {
//
//        FixedRateSampler sampler = new FixedRateSampler(100);
//        ExecutorService threadpool = Executors.newScheduledThreadPool(100);
//        for (int i = 0; i < 10; i++) {
//            threadpool.submit(() -> {
//                for (int j = 0; j < 10000; j++) {
//                    if (sampler.selfHit()) {
//                        System.out.println("my:"+p.incrementAndGet());
//                    }
//                    if(sampler.hit()){
//                        System.out.println("right:"+p_r.incrementAndGet());
//                    }
//                }
//            });
//        }
//        threadpool.shutdown();
//    }
//
//    public static Unsafe getUnsafe() {
//        try {
//            Field field = Unsafe.class.getDeclaredField("theUnsafe");
//            field.setAccessible(true);
//            return (Unsafe) field.get(null);
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    /**
//     * 自己写的
//     *
//     * @return
//     */
//    public synchronized boolean  selfHit() {
//        if ((U.getAndAddLong(this, SELFCOUNTER_ADDR, 1L)) % rate == 0) {
//            if (selfCounter > rate) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    public synchronized  boolean hit() {
//        int count = counter.incrementAndGet();//问题在这里,可能会同时增量多次
//        if (count >= rate) {
//            synchronized (counter) {
//                if (counter.get() >= rate) {
//                    counter.set(0);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
