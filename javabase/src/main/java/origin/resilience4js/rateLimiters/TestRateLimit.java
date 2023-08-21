package origin.resilience4js.rateLimiters;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.internal.AtomicRateLimiter;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

/**
 * @Author:lmq
 * @Date: 2020/2/5
 * @Desc:
 **/
@Log4j2
public class TestRateLimit {
    public static void main(String[] args) {
//        初始化高频控制器
//        高频控制 RateLimiter
        // 创建高频控制配置，控制频率为1QPS
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(1)
                .limitRefreshPeriod(Duration.ofMillis(1000))
                .timeoutDuration(Duration.ofMillis(500))
                .build();
// 创建高频控制注册中心
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
// 从注册中心创建高频控制器实例
//        RateLimiter rateLimiterWithDefaultConfig = rateLimiterRegistry.rateLimiter("backend");
//        RateLimiter rateLimiterWithCustomConfig = rateLimiterRegistry.rateLimiter("backend#2", config);
// 直接创建高频控制器实例
        RateLimiter rateLimiter = RateLimiter.of("NASDAQ :-)", config);

//        高频控制器使用方式 使用上面定义的高频控制器装饰函数调用
        CheckedRunnable restrictedCall = RateLimiter.decorateCheckedRunnable(rateLimiter, () -> System.out.println("Do something"));
// 第一次调用成功，第二次调用被高频限制
        Try.run(restrictedCall).andThenTry(restrictedCall).onFailure(throwable -> System.out.println("Wait before call it again :)"));
//        你可以在运行时动态修改高频控制器配置，但新的冷却时间不会影响当前处于冷却状态的线程，新的阈值也不会影响处于当前一轮控制的线程： 在下一轮控制中，阈值变更为100
        rateLimiter.changeLimitForPeriod(100);

//        监听高频控制事件
//        高频控制器事件RateLimiterEvent包含允许执行和拒绝执行事件，所有事件包含发生时间、相关高频控制器名称的信息。
        rateLimiter.getEventPublisher().onSuccess(event -> log.info(event)).onFailure(event -> log.info(event));

        //
        RateLimiter limit = rateLimiter;
        RateLimiter.Metrics metrics = limit.getMetrics();
        int numberOfThreadsWaitingForPermission = metrics.getNumberOfWaitingThreads();
        int availablePermissions = metrics.getAvailablePermissions();
        AtomicRateLimiter atomicLimiter = new AtomicRateLimiter("hha",config);
        long nanosToWaitForPermission = atomicLimiter.getDetailedMetrics().getNanosToWait();
    }
}
