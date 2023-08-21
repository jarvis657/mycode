package origin.concurrency;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author muqi.lmq
 * @date 2018/7/10.
 */
public class TurnPrintByThread {
    private static final Unsafe U;
    private static final long SELFCOUNTER_ADDR;
    static int num = 0;
    static volatile int turn = 0;

    static {
        try {
            U = getUnsafe();
            Class<?> k = TurnPrintByThread.IdContain.class;
            SELFCOUNTER_ADDR = U.objectFieldOffset(k.getDeclaredField("i"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

//    public static void main(String[] args) {
//        originTurnPrint();
////        mTurnPrint();
//    }

    private static String who(int i) {
        switch (i) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
        }
        return i + "";
    }

    private static int turnWho(int i) {
        switch (i) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 0;
        }
        return 0;
    }

    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
        }
        return null;
    }

    //wrong!!!!!!!!!!!
    private static void mTurnPrint() {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            IdContain id = new IdContain(i);
            Thread t0 = new Thread(() -> {
                for (; 100 > IdContain.num; ) {
                    if ((turn == id.get()) && (IdContain.num == 0 || ++IdContain.num % 3 == id.get())) {
                        System.out.println(who(id.get()) + "   id:" + id.get() + "   " + ":" + IdContain.num);
                        turn = turnWho(id.get());
                    }
                }
            }
            );
            ts.add(t0);
        }
        ts.stream().forEach(t -> t.start());
    }

    private static void originTurnPrint() {
        Thread t0 = new Thread(() -> {
            for (; 100 > num; ) {
                if ((turn == 0) && (num == 0 || ++num % 3 == 0)) {
                    System.out.println("A:" + num);
                    turn = 1;
                }
            }
        }
        );
        Thread t1 = new Thread(() -> {
            for (; 100 > num; ) {
                if ((turn == 1) && (++num % 3 == 1)) {
                    System.out.println("B:" + num);
                    turn = 2;
                }
            }
        }
        );
        Thread t2 = new Thread(() -> {
            for (; 100 > num; ) {
                if ((turn == 2) && (++num % 3 == 2)) {
                    System.out.println("C:" + num);
                    turn = 0;
                }
            }
        }
        );
        t1.start();
        t0.start();
        t2.start();
    }

    static class IdContain {
        static int num = 0;
        volatile int i;

        public IdContain(int i) {
            this.i = i;
        }

        public int get() {
            return U.getInt(this, SELFCOUNTER_ADDR);
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }
    }
    //其他简单方式
    private final Object monitor = new Object();
    private final int limit;
    private volatile int count;

    public TurnPrintByThread(int limit, int initCount) {
        this.limit = limit;
        this.count = initCount;
    }

//    public void print() {
//        synchronized (monitor) {
//            while (count < limit) {
//                try {
//                    System.out.println(String.format("线程[%s]打印数字:%d", Thread.currentThread().getName(), ++count));
//                    monitor.notifyAll();
//                    monitor.wait();
//                } catch (InterruptedException e) {
//                    //ignore
//                }
//            }
//        }
//    }
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void print()  {
        lock.lock();
        try {
            while (count < limit){
                System.out.println(String.format("线程[%s]打印数字:%d", Thread.currentThread().getName(), ++count));
                condition.signalAll();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) throws Exception {
        TurnPrintByThread printer = new TurnPrintByThread(10, 0);
        Thread thread1 = new Thread(printer::print, "thread-1");
        Thread thread2 = new Thread(printer::print, "thread-2");
        thread1.start();
        thread2.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
