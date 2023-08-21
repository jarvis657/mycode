package origin.resilience4js;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.function.Supplier;

/**
 * @Author:lmq
 * @Date: 2020/2/5
 * @Desc:
 **/
@Log4j2
public class commonTest {
    public static void main(String[] args) {
        Supplier<String> supplier = () -> "haha";
// In order to run this code, you will need resilience4j-all (See below)
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
                .withRetry(Retry.ofDefaults("name"))
                .withCircuitBreaker(CircuitBreaker.ofDefaults("name"))
                .withBulkhead(Bulkhead.ofDefaults("name")).decorate();
        String result = Try.ofSupplier(decoratedSupplier).recover(throwable -> "Hello from Recovery").get();
    }
}
