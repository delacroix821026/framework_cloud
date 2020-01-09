package com.fw.gateway.convert;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Map;

public class GithubUserAuthenticationConverter extends DefaultUserAuthenticationConverter implements UserAuthenticationConverter {
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
        Map<String, ?> map = super.convertUserAuthentication(userAuthentication);
        if(userAuthentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) userAuthentication;
            map.put("openId", token.getPrincipal().getAttribute("id"));
        }
        return map;
    }
}
