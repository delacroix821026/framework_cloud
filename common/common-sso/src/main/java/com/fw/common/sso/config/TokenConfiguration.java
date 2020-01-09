package com.fw.common.sso.config;

import com.fw.common.sso.service.impl.ResourceUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@ConditionalOnMissingClass({"com.fw.oauth.config.SecurityConfiguration"})
public class TokenConfiguration {
    @Bean
    public ResourceUserDetailsService getResourceUserDetailsService() {
        return new ResourceUserDetailsService();
    }

    @Autowired
    public void remoteTokenServices(@Qualifier("remoteTokenServices") RemoteTokenServices remoteTokenServices) {
        DefaultUserAuthenticationConverter converter = new DefaultUserAuthenticationConverter();
        converter.setUserDetailsService(getResourceUserDetailsService());

        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
        tokenConverter.setUserTokenConverter(converter);

        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setAccessTokenConverter(tokenConverter);
        jwtAccessTokenConverter.setSigningKey("DelacroixDesignIn2019");
        remoteTokenServices.setAccessTokenConverter(jwtAccessTokenConverter);
    }
}
