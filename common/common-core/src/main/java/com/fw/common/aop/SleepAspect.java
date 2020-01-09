package com.fw.common.aop;

import com.fw.common.aop.annotation.SleepAnnotation;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class SleepAspect {
    @Before("@annotation(sleep)")
    public void beforeTask(SleepAnnotation sleep) throws InterruptedException {
        //Sleep sleep = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Sleep.class);
        Thread.currentThread().sleep(sleep.value() * 1000);
    }
}
