package com.fw.common.sso.config;

import com.fw.common.sso.Security.AuthInvocationSecurityMetadataSource;
import com.fw.common.sso.Security.RequestMapProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromisionConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "fw.security")
    public RequestMapProperties requestMapProperties() {
        return new RequestMapProperties();
    }

    @Bean
    @RefreshScope
    public AuthInvocationSecurityMetadataSource metadataSource() {
        return new AuthInvocationSecurityMetadataSource(requestMapProperties());
    }

}
