package rsockets;

//import io.rsocket.AbstractRSocket;
//import io.rsocket.Payload;
//import io.rsocket.RSocketFactory;
//import io.rsocket.transport.netty.server.TcpServerTransport;
//import io.rsocket.util.DefaultPayload;
//import reactor.core.Disposable;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;

import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @Author:qishan
 * @Date: 2019-08-08
 * @Desc:
 **/
public class Server {
    private static final int TCP_PORT = 7777;
//    private final Disposable server;

//    public Server() {
//        this.server = RSocketFactory.receive()
//                .acceptor((setupPayload, reactiveSocket) -> Mono.just(new RSocketImpl()))
//                .transport(TcpServerTransport.create("localhost", TCP_PORT))
//                .start()
//                .subscribe();
//    }

    public static void main(String[] args) {
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);
        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
        System.out.println("range=========================");
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
//        Flux.interval(Duration.of(100,ChronoUnit.MILLIS)).buffer(Duration.of(1001,ChronoUnit.MILLIS)).take(2).toStream().forEach(System.out::println);
//        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
//        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);

//        Flux.range(1, 100).window(20).subscribe(System.out::println);
//        Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).window(Duration.of(101, ChronoUnit.MILLIS)).take(2).toStream().forEach(System.out::println);
////        Server server = new Server();
//        Flux.just("a", "b")
//                .zipWith(Flux.just("c", "d", "e"))
//                .subscribe(System.out::println);
//        Flux.just("a", "b")
//                .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2))
//                .subscribe(System.out::println);
//
//        Flux.range(1, 1000).take(10).subscribe(System.out::println);
//        Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
//        Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
//        Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);
//        System.out.println("===================================");
//        Flux.merge(Flux.interval(Duration.of(0, ChronoUnit.MILLIS), Duration.of(100, ChronoUnit.MILLIS)).take(5),
//                Flux.interval(Duration.of(50, ChronoUnit.MILLIS), Duration.of(100, ChronoUnit.MILLIS)).take(5))
//                .toStream()
//                .forEach(System.out::println);
//        Flux.mergeSequential((Flux.interval(Duration.of(0, ChronoUnit.MILLIS), Duration.of(100, ChronoUnit.MILLIS)).take(5),
//                Flux.interval(Duration.of(50, ChronoUnit.MILLIS), Duration.of(100, ChronoUnit.MILLIS)).take(5))
//                .toStream()
//                .forEach(System.out::println);
//        System.out.println("===================================");
//        Flux.just(1, 2)
//                .concatWith(Mono.error(new IllegalStateException()))
////                .onErrorReturn(0)
////                .switchOnError(Mono.just(0))
////                .retry(2)
//                .onErrorResume(e -> {
//                    if (e instanceof IllegalStateException) {
//                        return Mono.just(0);
//                    } else if (e instanceof IllegalArgumentException) {
//                        return Mono.just(-1);
//                    }
//                    return Mono.empty();
//                })
//                .subscribe(System.out::println);
//

        Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).subscribe(System.out::println);
        System.out.println("========scheduler==============");
//        当前线程，通过 Schedulers.immediate()方法来创建。
//        单一的可复用的线程，通过 Schedulers.single()方法来创建。
//        使用弹性的线程池，通过 Schedulers.elastic()方法来创建。线程池中的线程是可以复用的。当所需要时，新的线程会被创建。如果一个线程闲置太长时间，则会被销毁。该调度器适用于 I/O 操作相关的流的处理。
//        使用对并行操作优化的线程池，通过 Schedulers.parallel()方法来创建。其中的线程数量取决于 CPU 的核的数量。该调度器适用于计算密集型的流的处理。
//        使用支持任务调度的调度器，通过 Schedulers.timer()方法来创建。
//        从已有的 ExecutorService 对象中创建调度器，通过 Schedulers.fromExecutorService()方法来创建。
//        某些操作符默认就已经使用了特定类型的调度器。比如 intervalMillis()方法创建的流就使用了由 Schedulers.timer()创建的调度器。通过 publishOn()和 subscribeOn()方法可以切换执行操作的调度器。其中 publishOn()方法切换的是操作符的执行方式，而 subscribeOn()方法切换的是产生流中元素时的执行方式。

        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName()+"_next");
            sink.complete();
        })
                .publishOn(Schedulers.single())
                .map(x -> String.format("(%s) #%s#", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("{%s} @%s@", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel())
                .toStream()
                .forEach(System.out::println);

//        StepVerifier.create(Flux.just("a", "b"))
//                .expectNext("a")
//                .expectNext("b")
//                .verifyComplete();
    }

//    public void dispose() {
//        this.server.dispose();
//    }
//
//    private class RSocketImpl extends AbstractRSocket {
//        @Override
//        public Mono<Payload> requestResponse(Payload payload) {
//            try {
////                return Mono.just(DefaultPayload.create(String.format("Hello %s!", payload.getDataUtf8())));
//                return Mono.just(payload); // reflect the payload back to the sender
//            } catch (Exception x) {
//                return Mono.error(x);
//            }
//        }
//
//        @Override
//        public Flux<Payload> requestStream(Payload payload) {
//            return Flux.just("first", "second", "third", "fourth").map(text -> DefaultPayload.create(text));
//        }
////        @Override
////        public Mono<Void> fireAndForget(Payload payload) {
////            try {
////                dataPublisher.publish(payload); // forward the payload
////                return Mono.empty();
////            } catch (Exception x) {
////                return Mono.error(x);
////            }
////        }
//    }
}
