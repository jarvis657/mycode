package org.slf4j;

import brave.Tracing;
import brave.propagation.ThreadLocalCurrentTraceContext;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Author:lmq
 * @Date: 2022/4/7
 * @Desc:
 **/
public class TraceLog {

    /**
     * CompletableFuture<Void> smartFuture = CompletableFuture.runAsync(tracing.currentTraceContext().wrap(() -> {
     *     var tracer = tracing.tracer();
     *     var span = getNextSpan(tracer, "spanName");
     *     try (var ignored = tracer.withSpanInScope(span)) {
     *         // biz code
     *     } catch (Exception e) {
     *         span.error(e);
     *         throw e;
     *     } finally {
     *         span.finish();
     *     }
     * }), THREAD_POOL);
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
//           Tracing.newBuilder().currentTraceContext(MDCCurrentTraceContext.create()).build();
        Tracing tracing = Tracing.newBuilder().currentTraceContext(ThreadLocalCurrentTraceContext.create()).build();
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setMaxPoolSize(100);
        threadPoolTaskExecutor.setQueueCapacity(100);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setThreadNamePrefix("thread-prefix");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // decorate runnable used in thread pool
        threadPoolTaskExecutor.setTaskDecorator(tracing.currentTraceContext()::wrap);
        return threadPoolTaskExecutor;
    }

}
