package com.fw.gateway.config;

import com.fw.gateway.authority.RequestMapProperties;
import com.fw.gateway.jwt.JwtOAuth2AuthenticationTokenConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Map;

@Configuration
public class PromisionConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "fw.security")
    public RequestMapProperties requestMapProperties() {
        return new RequestMapProperties();
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws NoSuchFieldException, IllegalAccessException {
        RequestMapProperties properties = requestMapProperties();
        for (Map.Entry<String, String> entry : properties.getRequestMaps().entrySet()) {
            if (entry.getValue().equals("permitAll")) {
                http.authorizeExchange().pathMatchers(entry.getKey()).permitAll();
            } else if (entry.getValue().equals("denyAll")) {
                http.authorizeExchange().pathMatchers(entry.getKey()).denyAll();
            } else {
                http.authorizeExchange().pathMatchers(entry.getKey()).hasAuthority(entry.getValue());
            }

        }

        http.authorizeExchange().pathMatchers("/actuator/**").permitAll().and().csrf().disable();

        ServerBearerTokenAuthenticationConverter converter = new ServerBearerTokenAuthenticationConverter();

        http
                .authorizeExchange()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2Login()
                //.authenticationManager(new ToFwLoginReactiveAuthenticationManager())
                //.authenticationFailureHandler()
                //.authenticationSuccessHandler(handler)
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(new JwtOAuth2AuthenticationTokenConverter());
        return http.build();
    }

    @Bean
    Signer getSigner() {
        return new RsaSigner("-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIBOwIBAAJBAKU0lasI+gppPyO2yQ+hb+XQ1FVS4FRiQwLMxE9JWUeLeJQz4hZ9\n" +
                "vO8krmcsphadTmaXqi5SBcHRkgPWfQTLn6sCAwEAAQJAGYsQ3RoNNxDgz0/StJT8\n" +
                "bSclvW+L+eAbhlJMxAmOQU5iipRTPyuJXzIKplxploAaPNZtwqeFifT/8uP/Cgux\n" +
                "cQIhANm+OGTLMaLYlgAEzqntndaz3YdbzNEeRFfg9E81ZnejAiEAwjtLWnFMvr2E\n" +
                "QEYLbG9+BYGlyI1QrUmfnyT7V/mEWFkCIQDU6mCSju6WYlsq4YiEOUniLDcuqOF6\n" +
                "irIhjGLZIUp1KQIhAJG6UTAbf9xAbwCWTS3ffOr+ufb96AEMEyIhah84i46JAiAo\n" +
                "4ZnY0DzTAtnNgt9HPA5VkzxYXTkP/X0czFlvXhF0fg==\n" +
                "-----END RSA PRIVATE KEY-----");
    }
}
