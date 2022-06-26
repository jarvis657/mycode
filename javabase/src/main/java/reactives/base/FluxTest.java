package reactives.base;


import static jodd.util.ThreadUtil.sleep;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.scheduler.Schedulers;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/26
 * @Desc:
 **/
public class FluxTest {

    @Test
    public void flux_test() {
        Flux.create(emitter -> {
                    for (int i = 0; i < 100; i++) {
                        if (emitter.isCancelled()) {
                            return;
                        }
                        sleep(1000);
                        System.out.println("source created " + i);
                        emitter.next(i);
                    }
                }, OverflowStrategy.LATEST).onBackpressureBuffer(4).doOnNext(s -> {
                    System.out.println("source pushed " + s);
                }).publishOn(Schedulers.single())
                .subscribe(consumer -> {
                    sleep(2000);
                    System.out.println("comusmer 获取 id " + consumer);
                });
        sleep(100000);
    }
}
