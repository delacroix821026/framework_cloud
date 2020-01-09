package com.fw.gateway.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class JwtOAuth2User extends DefaultOAuth2User {
    private final String jwtTokenValue;
    public JwtOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String jwtTokenValue) {
        super(authorities, attributes, nameAttributeKey);
        this.jwtTokenValue = jwtTokenValue;
    }

    public String getJwtTokenValue() {
        return jwtTokenValue;
    }
}
