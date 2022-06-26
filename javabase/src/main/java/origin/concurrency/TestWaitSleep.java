package origin.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * wait 必须获得锁,sleep则不用
 */
public class TestWaitSleep {
    private static Object m = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread_a = new Thread(() -> {
            synchronized (m) {
                try {
                    System.out.println("======m wait===============");
                    m.wait();
                    System.out.println("=======m done=============");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("a error");
                }
                System.out.println("=======m done.........=============");
            }
        });
        thread_a.start();
        Thread thread_b = new Thread(() -> {
            try {
                System.out.println("==============m'  wait=====");
                synchronized (m) {
                    m.wait();
                    System.out.println("=======m' done=============");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("b error");
            }
            System.out.println("==============m'  doning.........=====");
        });
        TimeUnit.SECONDS.sleep(10);
        synchronized (m) {
            System.out.println("notifying.............all");
            m.notifyAll();
        }
        thread_b.start();
        TimeUnit.SECONDS.sleep(20);
        System.out.println(".....................");
    }
}
