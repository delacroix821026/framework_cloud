package com.fw.common.sso.config;

import com.fw.common.sso.extractor.AuthoritiesExtractor;
import com.fw.common.sso.extractor.FwPrincipalExtractor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SsoAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public FwPrincipalExtractor principalExtractor() {
        return new FwPrincipalExtractor();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthoritiesExtractor authoritiesExtractor() {
        return new AuthoritiesExtractor();
    }

}
