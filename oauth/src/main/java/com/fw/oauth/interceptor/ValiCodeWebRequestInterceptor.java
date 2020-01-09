package com.fw.oauth.interceptor;

import com.fw.common.service.CacheService;
import com.fw.oauth.exception.LockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order
public class ValiCodeWebRequestInterceptor implements WebRequestInterceptor, Filter {
    @Value("${fw.login.lock}")
    private int LOCK = 6;

    private static final String parameterUserName = "username";

    @Autowired
    private CacheService cacheService;

    @Override
    public void preHandle(WebRequest request) throws Exception {
        String username = request.getParameter(parameterUserName);
        process(username);
    }

    private void process(String username) throws LockException {
        log.debug("ValiCodeWebRequestInterceptor preHandle:");
        if (username != null && !"" .equals(username.trim())) {
            if (cacheService.exists(username)) {
                Integer countLoginFall = (Integer) cacheService.get(username);
                if (countLoginFall >= LOCK) {
                    throw new LockException("密码重试超过最大允许次数，账户锁定。", username);
                }
            }
        }
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        String username = request.getParameter(parameterUserName);
        if (username != null && !"" .equals(username.trim())) {
            cacheService.delete(username);
        }
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String username = servletRequest.getParameter(parameterUserName);
        try {
            process(username);
        } catch (LockException ex) {
            if (servletResponse instanceof HttpServletResponse) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
