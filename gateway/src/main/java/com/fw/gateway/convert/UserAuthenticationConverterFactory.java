package com.fw.gateway.convert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class UserAuthenticationConverterFactory {
    @Value("${fw.plant.code}")
    private String plantCode;

    @Autowired
    private Signer signer;

    private JsonParser objectMapper = JsonParserFactory.create();

    private UserAuthenticationConverter githubConvert = new GithubUserAuthenticationConverter();

    private UserAuthenticationConverter plantConvert = new DefaultUserAuthenticationConverter();

    private UserAuthenticationConverter getUserAuthenticationConverter(String clientId) {
        if("github".equals(clientId)) {
            return githubConvert;
        } else {
            return plantConvert;
        }
    }

    public String getToken(OAuth2AccessToken token, OAuth2AuthenticationToken authentication) {
        String content;
        try {
            content = this.objectMapper.formatMap(convertAccessToken(token, authentication));
            log.info("Convert Token: " + content);
        } catch (Exception var5) {
            return null;
        }
        return JwtHelper.encode(content, signer).getEncoded();
    }

    private Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2AuthenticationToken authentication) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.putAll(getUserAuthenticationConverter(authentication.getAuthorizedClientRegistrationId()).convertUserAuthentication(authentication));

        if (token.getScopes() != null) {
            Set<String> set = new HashSet<>();
            set.add("select");
            response.put(AccessTokenConverter.SCOPE, set);
            //token.getScopes()
        }

        if (token.getExpiresAt() != null) {
            response.put(AccessTokenConverter.EXP, token.getExpiresAt().getEpochSecond() + 315360000);
        }

        response.put(AccessTokenConverter.CLIENT_ID, plantCode);
        return response;
    }
}
