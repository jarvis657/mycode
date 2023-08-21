package origin.concurrency.threads;

/**
 * @Author:lmq
 * @Date: 2020/7/23
 * @Desc:
 **/
public class InheritThreadLoalTest {
    static InheritableThreadLocal<String> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal();
    static ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        INHERITABLE_THREAD_LOCAL.set("父线程中使用 InheritableThreadLocal 设置变量");
        THREAD_LOCAL.set("父线程中使用 ThreadLocal 设置变量");
        Thread thread = new Thread(
                () -> {
                    // 能拿到设置的变量
                    System.out.println("从 InheritableThreadLocal 拿父线程设置的变量: " + INHERITABLE_THREAD_LOCAL.get());
                    // 打印为 null
                    System.out.println("从 ThreadLocal 拿父线程设置的变量: " + THREAD_LOCAL.get());
                }
        );
        thread.start();
        thread.join();
    }
}
