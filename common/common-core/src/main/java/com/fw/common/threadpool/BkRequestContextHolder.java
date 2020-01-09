package com.fw.common.threadpool;

import org.springframework.core.NamedThreadLocal;

import java.util.Map;

public class BkRequestContextHolder {
    private static final ThreadLocal<Map<String, Object>> requestAttributesHolder =
            new NamedThreadLocal<>("BkRequest attributes");

    public static void resetRequestAttributes() {
        requestAttributesHolder.remove();
    }

    public static void setUserInfoAttributesHolder(Map<String, Object> header) {
        requestAttributesHolder.set(header);
    }

    public static Map<String, Object> getUserInfoAttributesHolder() {
        return requestAttributesHolder.get();
    }
}
