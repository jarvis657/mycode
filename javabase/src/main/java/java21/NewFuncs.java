package java21;

import static org.junit.Assert.assertTrue;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @Author:jarvis
 * @Date: 2023/9/28
 * @Desc:
 **/
public class NewFuncs {

    public static void main(String[] args) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://postman-echo.com/get"))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandler.asString());

        //进程API
        ProcessHandle self = ProcessHandle.current();
        long PID = self.pid();
        ProcessHandle.Info procInfo = self.info();
        Optional<String[]> argss = procInfo.arguments();
        Optional<String> cmd = procInfo.commandLine();
        Optional<Instant> startTime = procInfo.startInstant();
        Optional<Duration> cpuUsage = procInfo.totalCpuDuration();
        Stream<ProcessHandle> childProc = ProcessHandle.current().children();
        childProc.forEach(procHandle -> {
            assertTrue("Could not kill process " + procHandle.pid(), procHandle.destroy());
        });

        //try with resources
        MyAutoCloseable mac = new MyAutoCloseable();
        try (mac) {
            // do some stuff with mac
        }

//        API位于java.lang.invoke，由VarHandle和MethodHandles组成。它提供了java.util.concurrent.atomic和sun.misc.Unsafe对具有类似性能的对象字段和数组元素的操作。
//        java.util.concurrent.Flow提供支持反应流发布-订阅框架的接口。这些接口支持跨多个运行在jvm上的异步系统的互操作性。 我们可以使用实用类SubmissionPublisher来创建自定义组件

    }


    static class MyAutoCloseable implements Closeable {

        @Override
        public void close() throws IOException {

        }
    }
}
