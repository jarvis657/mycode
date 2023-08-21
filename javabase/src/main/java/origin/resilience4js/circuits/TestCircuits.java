package origin.resilience4js.circuits;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.github.resilience4j.consumer.CircularEventConsumer;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.time.Duration;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.*;

/**
 * @Author:lmq
 * @Date: 2020/2/5
 * @Desc:
 **/
@Log4j2
public class TestCircuits {
    public static void main(String[] args) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().ignoreException(e->e instanceof RuntimeException)
                .recordException(e-> e instanceof IOException)
                .ringBufferSizeInClosedState(2)
                .ringBufferSizeInHalfOpenState(2)
                .waitDurationInOpenState(Duration.ofMillis(1000))
//                .recordFailure(throwable -> Match(throwable).of(
//                        Case($(instanceOf(WebServiceException.class)), true),
//                        Case($(), false)))
                .build();
        // 使用定制化配置创建熔断器注册中心
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        // 从注册中心获取使用默认配置的熔断器
//        CircuitBreaker circuitBreaker2 = circuitBreakerRegistry.circuitBreaker("otherName");
        // 从注册中心获取使用定制化配置的熔断器
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("uniqueName", circuitBreakerConfig);

        //监听熔断器事件 分类监听事件
        circuitBreaker.getEventPublisher()
                .onSuccess(log::info)
                .onError(log::info)
                .onIgnoredError(log::info)
                .onReset(log::info)
                .onStateTransition(log::info);
        // 监听所有事件
        circuitBreaker.getEventPublisher().onEvent(log::info);

        // 用熔断器包装函数
        CheckedFunction0<String> decoratedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> "This can be any method which returns: 'Hello");

// 链接其它的函数
        Try<String> result = Try.of(decoratedSupplier).map(value -> value + " world'");

// 如果函数链中的所有函数均调用成功，最终结果为Success<String>
//        assertThat(result.isSuccess()).isTrue();
//        assertThat(result.get()).isEqualTo("This can be any method which returns: 'Hello world'");

        CircuitBreaker anotherCircuitBreaker = CircuitBreaker.ofDefaults("anotherTestName");

// 用两个熔断器分别包装Supplier 和 Function
        CheckedFunction0<String> decoratedSupplier1 = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> "Hello");
        CheckedFunction1<String, String> decoratedFunction = CircuitBreaker.decorateCheckedFunction(anotherCircuitBreaker, (input) -> input + " world");

// 链接函数
        Try<String> result1 = Try.of(decoratedSupplier1).mapTry(decoratedFunction);
        // 模拟失败调用，并链接降级函数
        CheckedFunction0<String> checkedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {
            throw new RuntimeException("BAM!");
        });
        Try<String> result2 = Try.of(checkedSupplier).recover(throwable -> "Hello Recovery");
// 降级函数被调用，最终调用结果为成功
//        assertThat(result2.isSuccess()).isTrue();
//        assertThat(result2.get()).isEqualTo("Hello Recovery");

        CircularEventConsumer<CircuitBreakerEvent> ringBuffer = new CircularEventConsumer<>(10);
        circuitBreaker.getEventPublisher().onEvent(ringBuffer);
        List<CircuitBreakerEvent> bufferedEvents = ringBuffer.getBufferedEvents();

        //熔断器状态
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        float failureRate = metrics.getFailureRate();
        int bufferedCalls = metrics.getNumberOfBufferedCalls();
        int failedCalls = metrics.getNumberOfFailedCalls();


    }
}
