package com.fw.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Slf4j
public class GlobalFilterConfiguration {
    @Bean
    public GlobalFilter getGlobalClientIdFilter() {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .filter(Objects::nonNull)
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication instanceof OAuth2AuthenticationToken)
                .map(authentication -> {
                    OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
                    log.info("clientId:" + auth.getAuthorizedClientRegistrationId());
                    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
                    builder.header("clientId", auth.getAuthorizedClientRegistrationId());
                    ServerHttpRequest request = builder.build();
                    return exchange.mutate().request(request).build();
                })
                .switchIfEmpty(Mono.just(exchange))
                .flatMap(chain::filter);
    }

    @Bean
    public GlobalFilter getGlobalUserNameFilter() {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .filter(Objects::nonNull)
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication instanceof OAuth2AuthenticationToken)
                .map(authentication -> (OAuth2AuthenticationToken) authentication)
                .map(oAuth2Authentication -> oAuth2Authentication.getPrincipal())
                .filter(oAuth2User -> Objects.nonNull(oAuth2User) && oAuth2User instanceof DefaultOAuth2User)
                .map(o -> {
                    DefaultOAuth2User user = (DefaultOAuth2User) o;
                    log.info("userName:" + user.getName());
                    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
                    builder.header("userName", user.getName());
                    ServerHttpRequest request = builder.build();
                    return exchange.mutate().request(request).build();
                })
                .switchIfEmpty(Mono.just(exchange))
                .flatMap(chain::filter);
    }

}
