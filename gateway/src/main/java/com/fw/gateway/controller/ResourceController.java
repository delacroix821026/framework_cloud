package com.fw.gateway.controller;

import com.fw.common.model.LoginUserInfo;
import com.fw.common.service.IUserInfoService;
import com.fw.gateway.convert.UserAuthenticationConverterFactory;
import com.fw.gateway.exception.UnregisteredUserException;
import com.fw.gateway.jwt.FwOAuth2User;
import com.fw.gateway.jwt.JwtOAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * @author cendaijuan
 */
@RestController
@Slf4j
public class ResourceController {
    @Value("${fw.plant.code}")
    private String plantCode;

    @Autowired
    private UserAuthenticationConverterFactory factory;

    @Autowired
    private IUserInfoService userInfoService;



    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Mono<String> getUser() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(Objects::nonNull)
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication instanceof OAuth2AuthenticationToken)
                .map(authentication -> (OAuth2AuthenticationToken) authentication)
                .map(oAuth2Authentication -> {
                    OAuth2User oAuth2User = oAuth2Authentication.getPrincipal();
                    //获取token
                    String token = null;
                    if (oAuth2User != null && oAuth2User instanceof JwtOAuth2User) {
                        JwtOAuth2User user = (JwtOAuth2User) oAuth2User;
                        token = user.getJwtTokenValue();
                    } else if (oAuth2User != null && oAuth2User instanceof FwOAuth2User) {
                        FwOAuth2User user = (FwOAuth2User) oAuth2User;
                        token = user.getAccessTokenValue();

                        //中转非平台token
                        if (!plantCode.equals(oAuth2Authentication.getAuthorizedClientRegistrationId())) {
                            token = factory.getToken(user.getOAuth2AccessToken(), oAuth2Authentication);
                        }
                    }


                    LoginUserInfo userInfo = null;
                    if (oAuth2User.getAttribute("openId") != null) {
                        userInfo = userInfoService.loadUserByOpenId(oAuth2User.getAttribute("openId"));
                    } else {
                        userInfo = userInfoService.loadUserByUsername(oAuth2User.getName());
                    }

                    if (userInfo == null) {
                        throw new UnregisteredUserException(token);
                    }

                    return token;
                });
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Mono<Map<String, ?>> me() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(Objects::nonNull)
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication instanceof OAuth2AuthenticationToken)
                .map(authentication -> (OAuth2AuthenticationToken) authentication)
                .map(oAuth2Authentication -> {
                    OAuth2User oAuth2User = oAuth2Authentication.getPrincipal();
                    return oAuth2User.getAttributes();
                });
    }
}