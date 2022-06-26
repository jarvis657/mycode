package origin.concurrency;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:qishan
 * @Date: 2019-07-06
 * @Desc:
 **/
public class RLock {
    private static final ReentrantLock takeLock = new ReentrantLock();
    private static final Condition notEmpty = takeLock.newCondition();
    private static final Condition notEmpty2 = takeLock.newCondition();

    public static void main(String[] args) throws InterruptedException, IOException {
//        WatchService watchService = FileSystems.getDefault().newWatchService();
//        Path path = Paths.get("/Users");
//        path.register(watchService,)
        String test_assert = "123";
        assert test_assert.equalsIgnoreCase("1234"):"1234";
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("r1 trylock ===========================");
                takeLock.lock();
                System.out.println("r1 trylocked ===========================");
                try {
                    System.out.println("r1 begin sleep");
                    Thread.currentThread().sleep(8000);
                    System.out.println("notEmpty begin await");
                    notEmpty.await();
                    System.out.println("r1 running await after===========================");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    takeLock.unlock();
                    System.out.println("r1 unlock ===========================");
                }
                System.out.println("r1 done......");
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("r2 trylock ===========================");
                takeLock.lock();
                System.out.println("r2 trylocked ===========================");
                try {
                    System.out.println("r2 begin sleep");
                    Thread.currentThread().sleep(5000);
                    System.out.println("notEmpty2 begin await");
                    notEmpty2.await();
                    System.out.println("r2 running await after ===========================");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    takeLock.unlock();
                    System.out.println("r2 unlock ===========================");

                }
                System.out.println("r2 done......");
            }
        };
        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                System.out.println("r3 trylock ===========================");
                takeLock.lock();
                System.out.println("r3 trylocked ===========================");
                try {
                    System.out.println("r3========================sleep before");
                    Thread.currentThread().sleep(2000);
                    System.out.println("notEmpty2 begin singnal");
                    notEmpty2.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    takeLock.unlock();
                    System.out.println("r3 unlock ===========================");
                }
                System.out.println("r3 done......");
            }
        };
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);

        t3.start();
        System.out.println("t3 started");
        Thread.currentThread().sleep(9000);

        t1.start();
        System.out.println("t1 started");
        t2.start();
        System.out.println("t2 started");

    }
}
