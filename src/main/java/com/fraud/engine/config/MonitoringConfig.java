package com.fraud.engine.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConfig {

    @Bean
    public io.micrometer.core.aop.TimedAspect timedAspect(MeterRegistry registry) {
        return new io.micrometer.core.aop.TimedAspect(registry);
    }

    @Bean
    public HealthIndicator fraudEngineHealthIndicator() {
        return () -> Health.up()
                .withDetail("service", "Fraud Rule Engine")
                .withDetail("version", "1.0.0")
                .withDetail("status", "operational")
                .build();
    }
}
