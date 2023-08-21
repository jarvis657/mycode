package origin.concurrency;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author:qishan
 * @Date: 2019-05-09
 * @Desc:
 **/
public class InteruptExample {
    /**
     * 中断 只是一个标志位,通过相关方法设置,然后又有相关方法判断如果中断会抛出异常:线程内调用了wait、jion、sleep方法中的一种， 立马抛出一个 InterruptedException，且中断标志被清除，重新设置为false
     *
     * @param args
     * @throws InterruptedException
     */
    volatile static boolean flag = true;

    /**
     * 此逻辑是验证 Park与中断标识的关系
     * 结论是如果 这个线程 是中断, 则park会阻塞,不然就直接忽略park方法.
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
//        testInterrupted();
        testSleep();
        return;
    }

    private static void testSleep() throws InterruptedException {
        ThreadDemo04 t4 = new ThreadDemo04();
        t4.start();
        Thread.sleep(3000);
        t4.interrupt();
        flag = false;
        t4.join();
    }

    public static class ThreadDemo04 extends Thread {
        @Override
        public void run() {
            while (flag) {
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("本打印出现在第一个sleep()之后");
            LockSupport.park();
            System.out.println(Thread.interrupted());
//            System.out.println(Thread.interrupted());
//            LockSupport.park();
            System.out.println("本打印出现在第二个park()之后");
        }
    }

    private static void testInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("------");
            System.out.println(Thread.currentThread().isInterrupted());//输出false
//            Thread.currentThread().interrupt();//当前线程中断 A
            System.out.println("Thread.currentThread().isInterrupted():" + Thread.currentThread().isInterrupted());//输出true Thread.sleep(3000);//中断后执行sleep会抛出异常
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                System.out.println("interrupted reset");
////                e.printStackTrace();
//            }
            System.out.println("one parked");
            LockSupport.park();//如果A处调用了中断, 这个park不会起作用!!!!!!!!
            while (flag) {
            }
            System.out.println("checking thread is interupt  and reset it:" + Thread.interrupted());
            System.out.println("two parked");
            LockSupport.park();//如果A处调用了中断, 这个park不会起作用!!!!!!!!
            System.out.println("over final Thread.currentThread().isInterrupted():" + Thread.currentThread().isInterrupted());//输出true Thread.sleep(3000);//中断后执行sleep会抛出异常
        });
        thread.start();
        Thread.sleep(3000);
        System.out.println("thread is interupt " + thread.isInterrupted() + " invoke interupt");
        thread.interrupt();
        Thread.sleep(2000);
        flag = false;
//        thread.interrupt();
        LockSupport.unpark(thread);//调用 unpark 或者设置中断都可以
        thread.join();
    }

//    private ReentrantLock lock = new ReentrantLock();
//
//    private Condition condition = lock.newCondition();
//
//    public static void main(String[] args) throws Exception {
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                InteruptExample test = new InteruptExample();
//                test.say();
//            }
//        });
//        t.start();
//        t.interrupt();
////        for (int i = 0; i < 10; i++) {
////            new Thread(new Runnable() {
////                public void run() {
////                    InteruptExample test = new InteruptExample();
////                    test.say();
////                }
////            }).start();
////        }
//    }
//
//    public void say() {
//        try {
//            lock.lock();
//            int i = 0;
//            while (i < 3) {
//                try {
//                    i++;
//                    condition.await();
//                } catch (InterruptedException e) {
//                    System.out.println("interrupted");
//                    Thread.currentThread().interrupt();//问题.会一直循环
//                }
//            }
//        } finally {
//            lock.unlock();
//        }
//    }

}
