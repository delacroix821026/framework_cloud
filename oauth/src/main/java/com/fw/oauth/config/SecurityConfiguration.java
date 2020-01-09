package com.fw.oauth.config;

import com.fw.oauth.handler.LoginFailureHandler;
import com.fw.oauth.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**", "/*.ico").permitAll()
                //.anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .failureHandler(new LoginFailureHandler())
                //.successHandler(new LoginSuccessHandler())影响登陆成功后回跳callback
                .permitAll()
                /*.and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login.html"))*/
                .and().logout()
                .logoutSuccessUrl("/login.html").permitAll().and().csrf().disable();
        //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
                /*.and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);*/
        // @formatter:on
    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
            // @formatter:on
        }
    }
}
