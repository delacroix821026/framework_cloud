package com.fw.common.interceptor;

import com.fw.common.threadpool.BkRequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserInfoHandlerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        log.debug("request.getHeader(userName)= " + request.getHeader("userName"));
        log.debug("request.getHeader(clientId)= " + request.getHeader("clientId"));
        Enumeration headerNames =request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = String.valueOf(headerNames.nextElement());
            headers.put(headerName, request.getHeader(headerName));
        }
        BkRequestContextHolder.setUserInfoAttributesHolder(headers);
        return true;
    }
}
