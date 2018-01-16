package com.newland.financial.p2p.common.handler;

import feign.hystrix.FallbackFactory;

public class FeignFallbackFactoryHandler<T, K> implements FallbackFactory<K> {
    public K create(final Throwable cause) {

        return null;
    }
}
