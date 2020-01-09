package com.fw.gateway.jwt;

import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import reactor.core.publisher.Mono;

public class FwReactiveOAuth2UserService extends DefaultReactiveOAuth2UserService {
    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        return super.loadUser(userRequest).map(oAuth2User -> {
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                    .getUserNameAttributeName();
            return new FwOAuth2User(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), userNameAttributeName,
                    userRequest.getAccessToken());
        });
    }
}
