package meters;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.ThreadDeadlockDetector;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author:lmq
 * @Date: 2020/10/21
 * @Desc:
 **/
public class DropwizardMetrics {
    public static void main(String[] args) throws InterruptedException {
        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        healthCheckRegistry.register("thread-dead-hc-r", new ThreadDeadlockHealthCheck());
        MetricRegistry metricRegistry = new MetricRegistry();
        metricRegistry.registerAll(new GarbageCollectorMetricSet());
        metricRegistry.registerAll(new ThreadStatesGaugeSet());
        metricRegistry.registerAll(new ClassLoadingGaugeSet());
        metricRegistry.gauge("thread-dead-r", () -> healthCheckRegistry::runHealthChecks);


        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();
        reporter.start(10, TimeUnit.SECONDS);
        Thread.currentThread().join();
    }

}
