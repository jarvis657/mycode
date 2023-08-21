package origin.resilience4js.bulkheads;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
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
public class TestBulkhead {
    public static void main(String[] args) {
        String s = "我®是中国ƍ";
        System.out.println(s.length());
        System.out.println(s.charAt(1));
//        初始化Bulkhead
//        Bulkhead的配置方式与熔断类似，有对应的BulkheadRegistry 和 BulkheadConfig，自定义配置的可选项有：
//        最大并行度
//        尝试进入饱和态的Bulkhead时，线程的最大阻塞时间
// 创建自定义的Bulkhead配置
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(150)
                .maxWaitDuration(Duration.ofMillis(500))
                .build();
        Bulkhead bulkhead1_ = Bulkhead.ofDefaults("foo");
        Bulkhead bulkhead2_ = Bulkhead.of("bar", BulkheadConfig.custom().maxConcurrentCalls(50).build());
// 创建Bulkhead注册中心
        BulkheadRegistry registry = BulkheadRegistry.of(config);
// 从注册中心获取默认配置的Bulkhead

        Bulkhead bulkhead1 = registry.bulkhead("foo");
// 从注册中心获取自定义配置的Bulkhead
        Bulkhead bulkhead = registry.bulkhead("bar", config);

        // 用Bulkhead包装函数调用
        CheckedFunction0<String> decoratedSupplier = Bulkhead.decorateCheckedSupplier(bulkhead, () -> "This can be any method which returns: 'Hello");

// 链接其它函数
        Try<String> result = Try.of(decoratedSupplier).map(value -> value + " world'");
//        assertThat(result.isSuccess()).isTrue();
//        assertThat(result.get()).isEqualTo("This can be any method which returns: 'Hello world'");
//        assertThat(bulkhead.getMetrics().getAvailableConcurrentCalls()).isEqualTo(1);
        // 用两个Bulkhead分别包装Supplier 和 Function
        CheckedFunction0<String> decoratedSupplier1 = Bulkhead.decorateCheckedSupplier(bulkhead, () -> "Hello");
        CheckedFunction1<String, String> decoratedFunction = Bulkhead.decorateCheckedFunction(bulkhead, (input) -> input + " world");

// 链接函数
        Try<String> result2 = Try.of(decoratedSupplier).mapTry(decoratedFunction);
//        assertThat(result.isSuccess()).isTrue();
//        assertThat(result.get()).isEqualTo("Hello world");
//        assertThat(bulkhead.getMetrics().getAvailableConcurrentCalls()).isEqualTo(1);
//        assertThat(anotherBulkhead.getMetrics().getAvailableConcurrentCalls()).isEqualTo(1);
        test2();
        bulkhead.getEventPublisher()
                .onCallPermitted(log::info)
                .onCallRejected(log::info)
                .onCallFinished(log::info);
    }

    public static void test2() {
        // 创建并行度为2的Bulkhead
        BulkheadConfig config = BulkheadConfig.custom().maxConcurrentCalls(2).build();
        Bulkhead bulkhead = Bulkhead.of("test", config);
// 该方法会进入Bulkhead
        bulkhead.acquirePermission();
        System.out.println("1");
        bulkhead.acquirePermission();
        System.out.println("2");
//        bulkhead.tryAcquirePermission();
//        System.out.println("3");

// 经过上面两次调用，Bulkhead已饱和
        CheckedRunnable checkedRunnable = Bulkhead.decorateCheckedRunnable(bulkhead, () -> {
            System.out.println("vvvvvvvvvvvvvvvv");
            throw new RuntimeException("BAM!");
        });
        Try result = Try.run(checkedRunnable);
        System.out.println("result:");
        System.out.println(result.getCause());
//        assertThat(result.isFailure()).isTrue();
//        assertThat(result.failed().get()).isInstanceOf(BulkheadFullException.class);
    }
}
