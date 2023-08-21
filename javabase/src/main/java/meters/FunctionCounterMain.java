package meters;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.ToDoubleFunction;

/**
 * @Author:lmq
 * @Date: 2020/8/26
 * @Desc:
 **/
//@Timed(value = "aws.scrape", longTask = true)
public class FunctionCounterMain {
    public static void main(String[] args) throws Exception {
        MeterRegistry registry = new SimpleMeterRegistry();
        AtomicInteger n = new AtomicInteger(0);
        //这里ToDoubleFunction匿名实现其实可以使用Lambda表达式简化为AtomicInteger::get
        FunctionCounter.builder("functionCounter", n, new ToDoubleFunction<AtomicInteger>() {
            @Override
            public double applyAsDouble(AtomicInteger value) {
                return value.get() + 1;
            }
        }).baseUnit("function")
                .description("functionCounter")
                .tag("createOrder", "CHANNEL-A")
                .register(registry);
        //下面模拟三次计数
        n.incrementAndGet();
        n.incrementAndGet();
        n.incrementAndGet();
        n.incrementAndGet();
        n.incrementAndGet();
        n.incrementAndGet();
        n.incrementAndGet();
        n.incrementAndGet();

//        Timer timer = Metrics.timer("timer", "createOrder", "cost");
//        timer.record(() -> {
//            try {
//                Thread.currentThread().sleep(10);
//            } catch (InterruptedException e) {
//            }
//        });
        Timer timer = Timer
                .builder("my.timer")
                .description("a description of what this timer does") // 可选
                .tags("region", "test") // 可选
                .register(registry);
        //这个是为了满足参数,暂时不需要理会
        Object holder = new Object();
        AtomicLong totalTimeNanos = new AtomicLong(0);
        AtomicLong totalCount = new AtomicLong(0);
        FunctionTimer.builder("functionTimer", holder, p -> totalCount.get(),
                p -> totalTimeNanos.get(), TimeUnit.NANOSECONDS)
                .register(new SimpleMeterRegistry());
        totalTimeNanos.addAndGet(10000000);
        totalCount.incrementAndGet();

//        LongTaskTimer longTaskTimer = registry.more().longTaskTimer("longTaskTimer");
//        longTaskTimer.record(() -> {
//
//            //这里编写Task的逻辑
//        });
//        //或者这样
//        Metrics.more().longTaskTimer("longTaskTimer").record(()-> {
//            //这里编写Task的逻辑
//        });

        List<String> list = registry.gauge("listGauge", Collections.emptyList(), new ArrayList<>(), List::size);
        list.add("1");
        list.add("2");
        list.add("3");
        List<String> list2 = registry.gaugeCollectionSize("listSize2", Tags.empty(), new ArrayList<>());
        list2.addAll(list);
        list2.add("4");
        Map<String, Integer> map = registry.gaugeMapSize("mapGauge", Tags.empty(), new HashMap<>());
        map.put("1", 1);
        AtomicInteger nn = registry.gauge("numberGauge", new AtomicInteger(0));
        nn.set(1);
        nn.set(2);
        nn.set(4);


        TimeGauge.Builder<AtomicInteger> timeGauge = TimeGauge.builder("timeGauge", n,
                TimeUnit.SECONDS, AtomicInteger::get);
        timeGauge.register(registry);
        n.addAndGet(10086);
        print(registry);
        n.set(1);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        print(registry);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        String key = "doge";
        String value = CACHE.get(key);
        record();
    }

    private static void print(MeterRegistry registry) {
        Search.in(registry).meters().forEach(each -> {
            StringBuilder builder = new StringBuilder();
            builder.append("name:")
                    .append(each.getId().getName())
                    .append(",tags:")
                    .append(each.getId().getTags())
                    .append(",type:").append(each.getId().getType())
                    .append(",value:").append(each.measure());
            System.out.println(builder.toString());
        });
    }


    private static final DistributionSummary DS  = DistributionSummary.builder("cacheHitPercent")
            .register(new SimpleMeterRegistry());

    private static final LoadingCache<String, String> CACHE = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .recordStats()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "ahaha";//selectFromDatabase();
                }
            });

    private static void record()throws Exception{
        CacheStats stats = CACHE.stats();
        BigDecimal hitCount = new BigDecimal(stats.hitCount());
        BigDecimal requestCount = new BigDecimal(stats.requestCount());
        DS.record(hitCount.divide(requestCount,2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
    }
}
