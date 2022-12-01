package ms.asp.appointment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ms.asp.appointment.config.filter.ReactiveSpringLoggingFilter;
import ms.asp.appointment.util.UniqueIDGenerator;

@Configuration
public class LoggingConfig {

    private String ignorePatterns;
    private boolean logHeaders;
    private boolean useContentLength = true;

    @Value("${spring.application.name:-}")
    String name;

    @Bean
    public UniqueIDGenerator generator() {
	return new UniqueIDGenerator();
    }

    @Bean
    @ConditionalOnExpression("${application.reactive-logging-filter.enabled:true}")
    public ReactiveSpringLoggingFilter reactiveSpringLoggingFilter() {
	return new ReactiveSpringLoggingFilter(generator(), ignorePatterns, logHeaders, useContentLength);
    }
}
