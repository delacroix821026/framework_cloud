package com.newland.financial.p2p.common.pointcut;

import com.newland.financial.p2p.common.util.AopTargetUtils;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author cengdaijuan
 */
@Aspect
@Component
@Log4j
public class LogProcessTimeCut {
    /**
     * 面向Service.
     */
    @Pointcut("execution(* com.newland.financial.p2p.service.*.*(..))")
    private void controllerAspect() {
    }

    /**
     * @param point ProceedingJoinPoint
     * @return object
     * @throws Throwable if has error
     */
    @Around("controllerAspect()")
    public final Object around(final ProceedingJoinPoint point)
            throws Throwable {
        String methodName = point.getSignature().getName();
        String className = AopTargetUtils.getTarget(point.getTarget()).getClass().getSimpleName();
        Long start = System.currentTimeMillis();
        Object result = point.proceed();
        Long useTime = System.currentTimeMillis() - start;

        log.info("Process Class:" + className + " - " + methodName + " in " + useTime + "ms");
        if (useTime > 3000)
            log.warn("Process Class:" + className + " - " + methodName + " need be  optimize!");
        return result;

    }
}