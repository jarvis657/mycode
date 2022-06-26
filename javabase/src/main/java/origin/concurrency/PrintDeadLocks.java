package origin.concurrency;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/23
 * @Desc:
 **/
public class PrintDeadLocks {
    public static void main(String[] args) throws InterruptedException {
        DealThread t1 = new DealThread();
        t1.setFlag("a");
        Thread thread1 = new Thread(t1);
        thread1.start();

        Thread.sleep(1000);

        t1.setFlag("b");
        Thread thread2 = new Thread(t1);
        thread2.start();

        Thread.sleep(4000);

        //获取xbean实例
        ThreadMXBean mBean = ManagementFactory.getThreadMXBean();
        //获取死锁的线程ID
        long[] dealThreads = mBean.findDeadlockedThreads();
        //遍历
        for (long pid : dealThreads) {
            //获取线程信息
            ThreadInfo threadInfo = mBean.getThreadInfo(pid);
            System.out.println(threadInfo);
        }
    }

    static class DealThread implements Runnable {

        public String username;
        public Object lock1 = new Object();
        public Object lock2 = new Object();

        public void setFlag(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            if ("a".equals(username)) {
                synchronized (lock1) {
                    try {
                        System.out.println("username= " + username);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (lock2) {
                        System.out.println("按lock1->lock2代码顺序执行了");
                    }
                }
            }
            if ("b".equals(username)) {
                synchronized (lock2) {
                    try {
                        System.out.println("username= " + username);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (lock1) {
                        System.out.println("按lock2->lock1代码顺序执行了");
                    }
                }
            }
        }
    }
}
