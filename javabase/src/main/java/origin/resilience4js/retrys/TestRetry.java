package origin.resilience4js.retrys;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.API;
import io.vavr.CheckedFunction0;
import io.vavr.Predicates;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import javax.xml.ws.WebServiceException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static io.vavr.API.$;

/**
 * @Author:lmq
 * @Date: 2020/2/5
 * @Desc:
 **/
@Log4j2
public class TestRetry {
    public static void main(String[] args) {
        // 最多重试2次
// 重试间隔为100ms
// 当执行出现WebServiceException时触发重试
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(5)
                .waitDuration(Duration.ofMillis(100))
                .retryOnException(throwable -> API.Match(throwable).of(
                        API.Case($(Predicates.instanceOf(WebServiceException.class)), true),
                        API.Case($(), false)))
                .build();
// 从重试配置创建重试器
        Retry retry = Retry.of("id", config);
// 使用重试器包装函数调用
        AtomicInteger ai = new AtomicInteger(0);
        CheckedFunction0<String> retryableSupplier = Retry.decorateCheckedSupplier(retry, () -> {
            System.out.println("try invoke");
            int i = ai.incrementAndGet();
            if (i < 4) {
                throw new WebServiceException("BAM!");
            }
            return "ok";
        });
// 调用将会自动重试
        Try<String> result = Try.of(retryableSupplier).recover((throwable) -> "Hello world from recovery function");
        System.out.println(result);
//        assertThat(result.get()).isEqualTo("Hello world from recovery function");
    }
}
