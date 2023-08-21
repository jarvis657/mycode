package origin.resilience4js.timeLimiters;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.control.Try;
import javax.xml.ws.WebServiceException;
import lombok.extern.log4j.Log4j2;

//import javax.xml.ws.WebServiceException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.*;

/**
 * @Author:lmq
 * @Date: 2020/2/5
 * @Desc:
 **/
@Log4j2
public class TestTimeLimiter {
    public static void main(String[] args) {
//        限时器与Future配合使用，限时器将Future Supplier转换为Callable，它将尝试在给定时间内获取Future的值，如果超时未获取到，Future将会被取消：
// 创建限时器配置，最大执行时间为60s，超时将取消Future
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(1))
                .cancelRunningFuture(true)
                .build();
// 从该配置创建限时器
        TimeLimiter timeLimiter = TimeLimiter.of(config);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
// 将待执行任务提交到线程池，获取Future Supplier
        Supplier<Future<Integer>> futureSupplier = () -> executorService.submit(() -> {
            Thread.currentThread().sleep(2000);
            System.out.println("vvvv");
            return 1;
        });
// 使用限时器包装Future Supplier
        Callable restrictedCall = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);
// 若任务执行超时，onFailure会被触发
        Try.of(restrictedCall::call).onFailure(throwable -> log.info("A timeout possibly occurred."));


//        你可以将限时器与熔断器配合使用，在超时次数过多时触发熔断：

        Callable restrictedCall2 = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .ringBufferSizeInClosedState(2)
                .ringBufferSizeInHalfOpenState(2)
                .waitDurationInOpenState(Duration.ofMillis(1000))
//                .recordFailure(throwable -> Match(throwable).of(
//                        Case($(instanceOf(WebServiceException.class)), true),
//                        Case($(), false)))
                .build();
        // 使用定制化配置创建熔断器注册中心
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        // 从注册中心获取使用定制化配置的熔断器
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("uniqueName", circuitBreakerConfig);
        Callable chainedCallable = CircuitBreaker.decorateCallable(circuitBreaker, restrictedCall2);
        Try.of(chainedCallable::call).onFailure(throwable -> log.info("We might have timed out or the circuit breaker has opened."));
        executorService.shutdown();
    }
}
