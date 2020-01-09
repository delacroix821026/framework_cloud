package com.fw.oauth.service.impl;

import com.fw.common.service.CacheService;
import com.fw.oauth.exception.LockException;
import com.fw.oauth.exception.TipsException;
import com.fw.oauth.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@Service
public class WebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator implements AuthenticationFailureHandler, AuthenticationSuccessHandler {
    @Value("${fw.login.tips}")
    private int TIPS = 3;

    @Value("${fw.login.lock}")
    private int LOCK = 6;

    @Autowired
    private CacheService cacheService;

    public ResponseEntity<OAuth2Exception> translate(Exception exception) throws Exception {
        //return super.translate(exception);
        return super.translate(processError(exception));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Exception ex = processError(exception);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        cacheService.delete(request.getParameter("username"));
    }

    private Exception processError(Exception exception) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (!(exception instanceof LockException)
                && !(exception instanceof UserNotFoundException)
                && request.getParameter("username") != null) {
            String username = request.getParameter("username");
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + 1, 0, 0, 0);
            if (cacheService.exists(username)) {
                Integer fallCount = (Integer) cacheService.get(username);
                fallCount = fallCount + 1;
                cacheService.set(username, fallCount, calendar.getTime());
                if (fallCount >= TIPS) {
                    if (fallCount < LOCK)
                        return new TipsException("账号登陆当日还可以重试" + (LOCK - fallCount) + "次！", username);
                    else
                        return new TipsException("账号登陆达到" + LOCK + "次！账号锁定", username);
                }
            } else {
                Integer fallCount = 1;
                cacheService.set(username, fallCount, calendar.getTime());
            }
        }

        return exception;
    }
}
