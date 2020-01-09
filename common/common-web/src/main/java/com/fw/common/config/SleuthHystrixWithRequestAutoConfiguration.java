package com.fw.common.config;

import brave.Tracing;
import com.fw.common.fegin.RequestContextHoldConcurrencyStrategy;
import com.netflix.hystrix.HystrixCommand;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.SpanNamer;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixConcurrencyStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration}
 * that registers a custom Sleuth {@link com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy}.
 *
 * @author Marcin Grzejszczak
 * @since 1.0.0
 *
 * @see SleuthHystrixConcurrencyStrategy
 */
@Configuration
@AutoConfigureAfter(TraceAutoConfiguration.class)
@ConditionalOnClass(HystrixCommand.class)
@ConditionalOnBean(Tracing.class)
@ConditionalOnProperty(value = "spring.sleuth.hystrix.strategy.enabled", matchIfMissing = true)
public class SleuthHystrixWithRequestAutoConfiguration {

	@Bean
	public RequestContextHoldConcurrencyStrategy requestContextHoldConcurrencyStrategy(Tracing tracing, SpanNamer spanNamer) {
		return new RequestContextHoldConcurrencyStrategy(tracing, spanNamer);
	}
}
