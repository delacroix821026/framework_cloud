package com.fw.common.aop;

import com.fw.common.aop.annotation.LogAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Aspect
@Slf4j
@Configuration
public class LogAspect {
    //private Date date;
    /*@Before("@annotation(logAnnotation)")
    public void beforeTask(JoinPoint point, LogAnnotation logAnnotation) throws InterruptedException {
        date = new Date();
        log.info(point.getSignature().toString());

        for (int i = 0; i < point.getArgs().length; i++) {
            Object obj = point.getArgs()[i];
            if (obj != null)
                log.info("Param[" + i + "]: " + point.getArgs()[i].toString());
            else
                log.info("Param[" + i + "]: null");
        }
    }

    @AfterReturning(value = "@annotation(logAnnotation)", returning = "returnValue")
    public void afterReturningTask(JoinPoint point, Object returnValue, LogAnnotation logAnnotation) {
        log.info(point.getSignature().toString() + ": execution time: " + String.valueOf((new Date()).getTime() - date.getTime()) + "(ms)");

        if (returnValue != null) {
            log.info("Result: " + returnValue.toString());
        }
    }*/

    @Around("@annotation(logAnnotation)")
    public Object aroundTask(ProceedingJoinPoint point, LogAnnotation logAnnotation) throws Throwable {
        Date date = new Date();
        try {
            log.info(point.getSignature().toString());

            for (int i = 0; i < point.getArgs().length; i++) {
                Object obj = point.getArgs()[i];
                if (obj != null)
                    log.info("Param[" + i + "]: " + point.getArgs()[i].toString());
                else
                    log.info("Param[" + i + "]: null");
            }
        } catch (Exception beforeEx) {
            log.error("print args exception", beforeEx);
        }

        Object retVal = null;
        try {
            retVal = point.proceed();
        } catch (Exception doEx) {
            try {
                if (log.isErrorEnabled()) {
                    log.error(point.getSignature().toString());

                    for (int i = 0; i < point.getArgs().length; i++) {
                        Object obj = point.getArgs()[i];
                        if (obj != null)
                            log.error("Param[" + i + "]: " + point.getArgs()[i].toString());
                        else
                            log.error("Param[" + i + "]: null");
                    }
                }
            } catch (Exception doPrintEx) {
                log.error("print process args exception", doPrintEx);
            }

            throw doEx;
        }

        try {
            log.info(point.getSignature().toString() + ": execution time: " + String.valueOf((new Date()).getTime() - date.getTime()) + "(ms)");

            if (retVal != null) {
                log.info("Result: " + retVal.toString());
            }
        } catch (Exception afterEx) {
            log.error("print result exception", afterEx);
        }

        return retVal;
    }
}
