package com.fw.gateway.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class FwOAuth2User extends DefaultOAuth2User {
    private final OAuth2AccessToken accessToken;

    public FwOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                        String nameAttributeKey, OAuth2AccessToken accessToken) {
        super(authorities, attributes, nameAttributeKey);
        this.accessToken = accessToken;
    }

    public String getAccessTokenValue() {
        return accessToken.getTokenValue();
    }

    public OAuth2AccessToken getOAuth2AccessToken() {
        return accessToken;
    }
}
