package com.fw.common.utils;

import com.fw.common.model.LoginUserInfo;
import com.fw.common.service.IUserInfoService;
import com.fw.common.threadpool.BkRequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Lazy
@Component
public class UserInfoUtils implements ApplicationContextAware {

    private static IUserInfoService service;

    public static LoginUserInfo getUser() {

        if (BkRequestContextHolder.getUserInfoAttributesHolder() != null) {
            Map<String, Object> headers = BkRequestContextHolder.getUserInfoAttributesHolder();
            if (headers != null) {
                LoginUserInfo loginUserInfo = (LoginUserInfo) headers.get("loginUserInfo");
                if (loginUserInfo != null)
                    return loginUserInfo;

                String userName = headers.get("userName") == null ? null : String.valueOf(headers.get("userName"));
                if (headers.get("userName") == null)
                    userName = headers.get("username") == null ? null : String.valueOf(headers.get("username"));
                log.info("BkRequestContextHolder getUser: userName - " + userName);

                loginUserInfo = service.loadUserByUsername(userName);
                headers.putIfAbsent("loginUserInfo", loginUserInfo);
                return loginUserInfo;
            }
        }

        return null;
    }

    public static String getClientId() {

        if (BkRequestContextHolder.getUserInfoAttributesHolder() != null) {
            Map<String, Object> headers = BkRequestContextHolder.getUserInfoAttributesHolder();
            if (headers != null) {
                String clientId = headers.get("clientId") == null ? null : String.valueOf(headers.get("clientId"));
                if (headers.get("clientId") == null)
                    clientId = headers.get("clientid") == null ? null : String.valueOf(headers.get("clientid"));
                log.info("BkRequestContextHolder clientId: userName - " + clientId);
                return clientId;
            }
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        UserInfoUtils.service = applicationContext.getBean(IUserInfoService.class);
    }
}
