package origin.spring;

import com.offbytwo.jenkins.JenkinsServer;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import origin.spring.converter.CustomStringToCollectionConverter;
import origin.spring.websocket.SocketProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author muqi.lmq
 * @date 2018/7/3.
 */

@Configuration
@EnableScheduling
@EnableAsync
@EnableWebSocket
//@AutoConfigureAfter(Configuration.class)
public class BaseConfiguration extends WebMvcConfigurerAdapter implements AsyncConfigurer, SchedulingConfigurer, WebSocketConfigurer {
//        @Autowired
//        private BaseConfig baseConfig;

    //    @Bean
//    public MethodValidationInterceptor MethodValidationInterceptor() {
//        return new MethodValidationInterceptor();
//    }
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
//            configurer.favorPathExtension(false).favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(true)
//                    .useJaf(false).defaultContentType(MediaType.APPLICATION_JSON)
//                    .mediaType("xml", MediaType.APPLICATION_XML).mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.removeConvertible(String.class, Collection.class);
        registry.addConverter(new CustomStringToCollectionConverter((GenericConversionService) registry));
    }
//     springboot 1.x下
//        @Bean
//       import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
//        public EmbeddedServletContainerCustomizer containerCustomizer() {
//            return container -> {
//                container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error"));
//                container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error"));
//                container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error"));
//            };
//        }
    //springboot 2 server.error.whitelabel.enabled=false
    //or
    //spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration
    //
    //#for Spring Boot 2.0
    //#spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
    //add @EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})


    @Bean(name = "baseAsyncExe")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(16);
        taskExecutor.setQueueCapacity(-1);
        taskExecutor.setMaxPoolSize(32);
        taskExecutor.setThreadNamePrefix("Base-Async");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> System.err.printf(String.format("[system]Unexpected error occurred invoking async method '%s'. error:{}", method), ex);
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    public Executor taskExecutor() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        return scheduledExecutorService;
    }


    @Bean
    public JenkinsServer getJenkinsServer() {
        try {
            return new JenkinsServer(new URI("url"), "", "");
        } catch (URISyntaxException e) {
        }
        return null;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketProcessor(), " /jobstatus").setAllowedOrigins("*");
    }

}
