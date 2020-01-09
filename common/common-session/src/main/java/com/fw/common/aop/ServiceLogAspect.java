package com.fw.common.aop;

import com.alibaba.fastjson.JSON;
import com.fw.common.model.UserInfo;
import com.fw.common.utils.UserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * 服务上添加日志输出
 */
//@Aspect
//@Component
@Slf4j
public class ServiceLogAspect {

    // 用@Pointcut来注解一个切入方法
    @Pointcut("execution(* com.fw.*.service..*.*(..))")
    public void executeService() {
    }

    /**
     * 调用方法前
     * @param joinPoint 切面
     */
    @Before("executeService()")
    public void before(JoinPoint joinPoint) {
        try {
            UserInfo userInfo = UserInfoUtils.getUser();
            if (null != userInfo && StringUtils.isNotEmpty(userInfo.getUsername())) {
                log.info(userInfo.getUsername() + " start access method: " + joinPoint.getSignature().getName());
            } else {
                log.info("Guest start access method: " + joinPoint.getSignature().getName());
            }
            Object[] arguments = joinPoint.getArgs();
            log.info("Method " + joinPoint.getSignature().getName() + "(); ");

            log.info("params： " + JSON.toJSONString(arguments));
        } catch (Exception e) {}
    }

    /**
     * 后置最终通知（目标方法只要执行完了就会执行后置通知方法）
     * @param joinPoint 切面
     */
    @After("executeService()")
    public void doAfterAdvice(JoinPoint joinPoint){
        UserInfo userInfo = UserInfoUtils.getUser();
        if (null != userInfo && StringUtils.isNotEmpty(userInfo.getUsername())) {
            log.info(userInfo.getUsername() + " end access method: " + joinPoint.getSignature().getName());
        } else {
            log.info("Guest end access method: " + joinPoint.getSignature().getName());
        }
    }


}