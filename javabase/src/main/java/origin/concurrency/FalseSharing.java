package origin.concurrency;

//import sun.misc.Contended;

//import jdk.internal.vm.annotation.Contended;

import jdk.internal.vm.annotation.Contended;

/**
 * @author muqi.lmq
 * @date 2018/4/22.
 */
public final class FalseSharing implements Runnable {
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    public static int NUM_THREADS = 4; // change
    private static VolatileLong[] longs;
    private final int arrayIndex;

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        Thread.sleep(10000);
        System.out.println("starting....");
        if (args.length == 1) {
            NUM_THREADS = Integer.parseInt(args[0]);
        }

        longs = new VolatileLong[NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    public final void test() {
    }

    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }
}

//-XX:-RestrictContended
//@Contended
class VolatileLong {
    public volatile long value = 0L;
}
//36018120029
//49359695226

//24653554058
//20652688242

//28607688256


//56573639296
//42873627382

