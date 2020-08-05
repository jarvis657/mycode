package origin.concurrency.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:lmq
 * @Date: 2020/5/13
 * @Desc:
 **/
public class ThreadTest {
    //    public static void main(String[] args) {
//        Thread thread = new Thread(()->{
//            int i =0;
//            while (i<3){
//                try {
//                    System.out.println("----");
//                    i++;
//                    Thread.currentThread().sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.setDaemon(true);//不用等待子线程
//        thread.start();
//        System.out.println("done");
//    }
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(1);
        Runnable r = () -> {
            System.out.println("==============");
            System.out.println(1 / 0);
            System.out.println("end==============");
        };
        service.execute(r);
        service.shutdown();
    }
}
