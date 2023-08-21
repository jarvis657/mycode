package reactives.base;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
//import reactor.tools.agent.ReactorDebugAgent;

/**
 * @Author:qishan
 * @Date: 2019/8/23
 * @Desc:
 **/
public class BackPressure {
    public static void main(String[] args) {
//        ReactorDebugAgent.init();
//        ReactorDebugAgent.processExistingClasses();
        test_pressure();
        String[] words = {"a", "b", "c", "e", "d", "f"};
        System.out.println();
        System.out.println();
        Flux.fromArray(words)                                                   // ①
                .zipWith(Flux.range(1, Integer.MAX_VALUE),                      // ②
                        (word, index) -> String.format("%s. %s", index, word)) // ③
                .subscribe(System.out::println);                                // ④
        System.out.println();
        System.out.println();
        System.out.println();
        Hooks.onOperatorDebug();
        try {
            Flux.range(1, 3)
                    .flatMap(n -> Mono.just(n + 100))
    //                .take(1)
                    .single()
                    .map(n -> n * 2)
                    .subscribe(System.out::println);
        } catch (Exception x) {
            System.out.println(x.getClass().getCanonicalName()+">>>>>>>>>>>>>");
            Throwable e = x;
            int i =0;
            while (e != null) {
                if (e.getClass().getSimpleName().contains("Timeout")) {
                    // Assume it's a connection timeout that would otherwise get lost: e.g. from JDBC 4.0
                    System.out.println("vvvvvvvvvvvvvvv");
                }
                e = e.getCause();
                System.out.println(i++);
            }
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        flux.limitRate(2).subscribe(System.out::println);
//        test_pressure();
        test_producer();
    }

    public static void test_pressure() {
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    int onNextAmount;
                    private Subscription s;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                        onNextAmount++;
                        if (onNextAmount % 2 == 0) {
                            s.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test_producer() {
        FluxProcessor<Integer, Integer> publisher = UnicastProcessor.create();
        publisher.doOnNext(event -> System.out.println("receive event: " + event)).subscribe();
        publisher.onNext(1); // print 'receive event: 1'
        publisher.onNext(2); // print 'receive event: 2'
    }

    public static void test_flux() {
        Scheduler FLUX_POOL = Schedulers.newParallel("e-threads", 20);
        long endTimeSec = System.currentTimeMillis() / 1000;
        final long[] p_endTimeSec = new long[]{endTimeSec};
        AtomicReference<List<Object>> paramsRevisionRef = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicInteger incr = new AtomicInteger(0);
        Flux.generate((SynchronousSink<Pair<Long, Long>> sink) -> {
            Pair<Long, Long> pair = new Pair(incr.get(), incr.get());
            if (incr.getAndIncrement() > 52) {
                sink.complete();
            } else {
                sink.next(pair);
            }
        }).parallel().runOn(FLUX_POOL).flatMap((Pair<Long, Long> pair) -> {
            Long key = pair.getKey();
            Long value = pair.getValue();
            Map<String, String> paramValues = new HashMap<>();
            try {
                List<Object> jsonObjects = new ArrayList<>();
                return Mono.just(jsonObjects);
            } catch (Exception e) {
                return Mono.error(e);
            }
        }).filter(rlist -> rlist.size() > 0).flatMap(lists -> {
            //封装后续请求参数
            Object[] objects = new Object[lists.size()];
            for (int i = 0; i < lists.size(); i++) {
                Object object = lists.get(i);
                //TODO
            }
            return Flux.just(objects);
        }).subscribe(new BaseSubscriber<Object>() {
            List<Object> os = new ArrayList<>(); //startEnds.stream().map(pair -> pair);
            private Subscription s;

            @Override
            public void hookOnSubscribe(Subscription s) {
                this.s = s;
                s.request(1);
            }

            @Override
            public void hookOnNext(Object jso) {
                s.request(1);
            }

            @Override
            public void hookOnError(Throwable t) {
                s.cancel();
                countDownLatch.countDown();
            }

            @Override
            public void hookOnComplete() {
                //TODO
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
        }
    }

    static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
