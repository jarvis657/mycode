package origin.concurrency.threads;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:lmq
 * @Date: 2020/5/13
 * @Desc:
 **/
public class ThreadTest {
    public static int ii = 0;
    public static List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (i < 10) {
                try {
                    System.out.println("----");
                    i++;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);//不用等待子线程
        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            System.out.println("vvvv");
//        }
        Thread.sleep(5000);
        System.out.println("done");
    }
//    public static void main(String[] args) {
////        ExecutorService service = Executors.newFixedThreadPool(1);
////        Runnable r = () -> {
////            System.out.println("==============");
////            System.out.println(1 / 0);
////            System.out.println("end==============");
////        };
////        service.execute(r);
////        service.shutdown();
//
//        for (int i : fortest()) {
//            System.out.println("pp");
//        }
//    }

    public static List<Integer> fortest() {
        System.out.println("---invoking-----");
        return list;
    }
}
