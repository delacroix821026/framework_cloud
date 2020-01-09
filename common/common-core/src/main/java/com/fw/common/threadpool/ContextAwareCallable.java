package com.fw.common.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class ContextAwareCallable<T> implements Callable<T> {
    private Callable<T> task;
    //private RequestAttributes requestAttributes;
    private Map<String, Object> headers = new HashMap<>();
    private SecurityContext securityContext;

    public ContextAwareCallable(Callable<T> task, SecurityContext securityContext) {
        this.task = task;
        this.securityContext = securityContext;
        headers = BkRequestContextHolder.getUserInfoAttributesHolder();
    }

    @Override
    public T call() throws Exception {
        if (headers != null) {
            BkRequestContextHolder.setUserInfoAttributesHolder(headers);
        }

        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }

        try {
            return task.call();
        } finally {
            try {
                BkRequestContextHolder.resetRequestAttributes();
            } catch (Exception e) {

            }

            try {
                SecurityContextHolder.clearContext();
            } catch (Exception e) {

            }
        }
    }

}
