package reactives.base;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.tools.agent.ReactorDebugAgent;

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
}
