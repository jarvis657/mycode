package origin.concurrency;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/23
 * @Desc:
 **/
public class WaitLocks {

    public static void main(String[] args) throws InterruptedException {
        Object scarce = new Object();
        Thread t = new Thread(new SyncWaiter(scarce));
        t.setName("Test-Thread-1");
        t.start();
        System.out.println("dddddddddddddd");
        Thread.currentThread().sleep(1000);
        System.out.println("xxxxxxxxxxxxxx");
        System.out.println("begin notifyAll");
        //必须加上锁才能调用notifyall.........不然notifyall会爆错
        synchronized (scarce) {
            scarce.notify();
        }
        System.out.println("end notifyall.........");
    }
}

class SyncWaiter implements Runnable {

    private Object scarce;

    public SyncWaiter(Object scarce) {
        this.scarce = scarce;
    }

    @Override
    public void run() {
        synchronized (scarce) {
            try {
                //这种情况下，线程处于WAITING状态，等待获取对象锁，该线程必须先锁定了该对象(即进入synchronized区域)，才可以调用wait方法(画重点)，并在调用wait方法后释放对象锁，并进入对象锁Wait Set队列等待被唤醒。WAITING状态的线程必须有对应的notify唤醒。这与后面遇到Blocked状态的线程不同，Blocked是不需要唤醒的。
                //备注：
                //1) 每一个Java对象与生俱来带有唯一一把对象锁(叫Intrinsic lock或Monitor)。
                //2) (a java.lang.Object)表示资源的类名，这里为一个Object对象。
                //3) 当调用带有时间参数的Wait方法，这时线程状态显示TIMED_WAITING。
                //4）Wait Set队列与下面的Entry Set队列的含义，可自行Google。
                System.out.println("wating...........");
                scarce.wait();
                System.out.println("notifyed........");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
