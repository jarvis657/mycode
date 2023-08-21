package origin.spring.webflux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
//import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * @author muqi.lmq
 * @date 2018/6/29.
 */
@RestController
public class WebFluxFileMain {
    public static void main(String[] args) {
        SpringApplication.run(WebFluxFileMain.class, args);
    }

//    @GetMapping(value = "files/{name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    Flux<String> files(@PathVariable String name) {
//        return null;
////        return Flux.create((FluxSink<String> sink) -> {
////            FluxSink<String> serialize = sink.next(name);
////            serialize.setCancellation(() -> filesChannel().unsubscribe(handler));
////            filesChannel().subscribe(handler);
////        });
//    }

//    @Bean
//    public IntegrationFlow inboundFlow(@Value("${input-dir:file://${HOME}/Desktop/in}") File in) {
//        System.out.println(in.getAbsolutePath());
//        return IntegrationFlows.from(Files.inboundAdapter(in).autoCreateDirectory(true), poller -> poller.poller(spec -> spec.fixedRate(1000L)))
//                .transform(File.class, File::getAbsolutePath)
//                .channel(filesChannel())
//                .get();
//    }

//    @Bean
//    SubscribableChannel filesChannel() {
//        return MessageChannels.publishSubscribe().get();
//    }
}
