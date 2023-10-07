package java21;

/**
 * @Author:jarvis
 * @Date: 2023/9/26
 * @Desc:
 **/
public class SimpleThread implements Runnable {

    @Override
    public void run() {
        System.out.println("当前线程名称：" + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
