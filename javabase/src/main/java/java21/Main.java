package java21;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;

/**
 * @Author:jarvis
 * @Date: 2023/9/26
 * @Desc:
 **/
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        Thread.ofPlatform().name("thread-test").start(new SimpleThread());
        Thread thread2 = Thread.startVirtualThread(new SimpleThread());
        Thread.ofVirtual()
                .name("thread-test")
                .start(new SimpleThread());
//或者
        Thread thread3 = Thread.ofVirtual()
                .name("thread-test")
                .uncaughtExceptionHandler((t, e) -> {
                    System.out.println(t.getName() + e.getMessage());
                })
                .unstarted(new SimpleThread());
        thread3.start();

        //factory
        ThreadFactory factory = Thread.ofVirtual().factory();
        Thread thread = factory.newThread(new SimpleThread());
        thread.setName("thread-test");
        thread.start();
        //executor
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        Future<?> submit = executorService.submit(new SimpleThread());
        Object o = submit.get();

        //any
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
//            Future<String> res1 = scope.fork(() -> runTask(1));
            Subtask<String> res2 = scope.fork(() -> runTask(2));
            Subtask<String> res3 = scope.fork(() -> runTask(3));
            scope.join();
            System.out.println("scope:" + scope.result());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        //shutdownfailed
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> res1 = scope.fork(() -> runTaskWithException(1));
            Subtask<String> res2 = scope.fork(() -> runTaskWithException(2));
            Subtask<String> res3 = scope.fork(() -> runTaskWithException(3));
            scope.join();
            scope.throwIfFailed(Exception::new);

            String s = res1.get();//或 res1.get()
            System.out.println(s);
            String result = Stream.of(res1, res2, res3)
                    .map(Subtask::get).collect(Collectors.joining());
            System.out.println("直接结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    public static String runTask(int i) throws InterruptedException {
        Thread.sleep(1000);
        long l = new Random().nextLong();
        String s = String.valueOf(l);
        System.out.println("第" + i + "个任务：" + s);
        return s;
    }

    // 有一定几率发生异常
    public static String runTaskWithException(int i) throws InterruptedException {
        Thread.sleep(1000);
        long l = new Random().nextLong(3);
        if (l == 0) {
            throw new InterruptedException();
        }
        String s = String.valueOf(l);
        System.out.println("第" + i + "个任务：" + s);
        return s;
    }
}
