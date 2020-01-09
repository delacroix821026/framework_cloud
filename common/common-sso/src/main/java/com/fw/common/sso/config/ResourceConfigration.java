package com.fw.common.sso.config;

import com.fw.common.sso.Security.AuthInvocationSecurityMetadataSource;
import com.fw.common.sso.Security.PromisionVoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableResourceServer
@ConditionalOnMissingClass({"com.fw.oauth.config.SecurityConfiguration"})//, "com.fw.zuul.config.SecurityConfiguration"
//@EnableOAuth2Client
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class ResourceConfigration extends ResourceServerConfigurerAdapter {
    @Value("${BK.RESOURCEID:bkproject}")
    private String resourceId;

    @Autowired(required = false)
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthInvocationSecurityMetadataSource metadataSource;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //todo 切换config
        resources.resourceId(resourceId).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                //.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    public <O extends FilterSecurityInterceptor> O postProcess(
                            O fsi) {
                        try {
                            fsi.setAuthenticationManager(authenticationManager);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((AbstractAccessDecisionManager) fsi.getAccessDecisionManager()).getDecisionVoters().add(new PromisionVoter());
                        metadataSource.setMetadataSource(fsi.getSecurityMetadataSource());
                        fsi.setSecurityMetadataSource(metadataSource);
                        return fsi;
                    }
                }).antMatchers("/hystrix*", "/auth/**")
                .permitAll()
                .antMatchers("/**/oauth/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .headers()
                .xssProtection()
                .xssProtectionEnabled(true);
    }

}
