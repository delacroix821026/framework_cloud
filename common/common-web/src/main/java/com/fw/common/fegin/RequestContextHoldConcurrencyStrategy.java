package com.fw.common.fegin;

import brave.Tracing;
import com.fw.common.threadpool.BkRequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.SpanNamer;
import org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixConcurrencyStrategy;

import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class RequestContextHoldConcurrencyStrategy extends SleuthHystrixConcurrencyStrategy {
    public RequestContextHoldConcurrencyStrategy(Tracing tracing, SpanNamer spanNamer) {
        super(tracing, spanNamer);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        if (callable instanceof RequestContextHoldConcurrencyStrategy.HystrixTraceCallable) {
            return callable;
        }
        Callable<T> wrappedCallable = super.wrapCallable(callable);
        Map<String, Object> headers = BkRequestContextHolder.getUserInfoAttributesHolder();
        return new RequestContextHoldConcurrencyStrategy.HystrixTraceCallable<>(wrappedCallable, headers);
    }

    static class HystrixTraceCallable<V> implements Callable<V> {
        private final Callable<V> delegate;

        private final Map<String, Object> headers;

        public HystrixTraceCallable(Callable<V> delegate, Map<String, Object> headers) {
            this.delegate = delegate;
            this.headers = headers;
        }

        @Override
        public V call() throws Exception {
            BkRequestContextHolder.setUserInfoAttributesHolder(headers);
            return this.delegate.call();
        }

    }
}
