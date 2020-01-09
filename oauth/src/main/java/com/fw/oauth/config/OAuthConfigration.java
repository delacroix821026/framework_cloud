package com.fw.oauth.config;

import com.fw.oauth.interceptor.ValiCodeWebRequestInterceptor;
import com.fw.oauth.service.UserInfoService;
import com.fw.oauth.service.impl.AuthEncodes;
import com.fw.oauth.service.impl.FwClientDetailsService;
import com.fw.oauth.service.impl.WebResponseExceptionTranslator;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.util.Base64;

@Configuration
@Slf4j
public class OAuthConfigration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private FwClientDetailsService clientDetailsService;

    @Autowired
    private ValiCodeWebRequestInterceptor interceptor;

    @Autowired
    private WebResponseExceptionTranslator translator;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        //允许表单认证
        oauthServer
                .tokenKeyAccess("isAuthenticated()")
                //.checkTokenAccess("isAuthenticated()")
                //.allowFormAuthenticationForClients()
                .passwordEncoder(new AuthEncodes());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .addInterceptor(interceptor)
                .exceptionTranslator(translator)
                //.tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter())
                //.accessTokenConverter(accessTokenConverter)
                //.exceptionTranslator()
                .userDetailsService(userInfoService)
                .authenticationManager(authenticationConfiguration.getAuthenticationManager())
                //.tokenGranter(tokenGranter(endpoints.getTokenGranter(), endpoints))
        ;
    }

    @Bean(name = "tokenStore")
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
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

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigner(getSigner());
        converter.setVerifierKey("-----BEGIN PUBLIC KEY-----\n" +
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKU0lasI+gppPyO2yQ+hb+XQ1FVS4FRi\n" +
                "QwLMxE9JWUeLeJQz4hZ9vO8krmcsphadTmaXqi5SBcHRkgPWfQTLn6sCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----");
        return converter;
    }

    @Bean
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:org/springframework/security/messages");
        return source;
    }

    public static void main(String[] arg) {
        String keyValue = "-----BEGIN PUBLIC KEY-----\n" +
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKU0lasI+gppPyO2yQ+hb+XQ1FVS4FRi\n" +
                "QwLMxE9JWUeLeJQz4hZ9vO8krmcsphadTmaXqi5SBcHRkgPWfQTLn6sCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----";
        /*try {
            //RSAPublicKeyImpl key = new RSAPublicKeyImpl(a.getBytes());
            //RSAPublicKeyImpl key = new RSAPublicKeyImpl(Base64.getDecoder().decode(keyValue));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }*/

        PemReader pemReader = new PemReader(new StringReader(keyValue));
        PemObject pem = null;
        try {
            pem = pemReader.readPemObject();
            new RSAPublicKeyImpl(pem.getContent());
        } catch (IOException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
