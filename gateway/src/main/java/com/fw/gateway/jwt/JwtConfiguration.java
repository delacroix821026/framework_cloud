package com.fw.gateway.jwt;

import com.fw.gateway.head.BasicAuthHead;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.function.client.WebClient;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtConfiguration.class);

    @Value("${spring.security.oauth2.client.provider.jsqy.jwk-set-uri}")
    private String keyUri;

    @Value("${spring.security.oauth2.client.registration.jsqy.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.jsqy.client-secret}")
    private String password;

    /*@Bean
    ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> userService(ReactiveJwtDecoder jwtDecoder){
        return new JwtReactiveOAuth2UserService(jwtDecoder);
    }*/

    @Bean
    ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
        return new FwReactiveOAuth2UserService();
    }


    @Bean
    ReactiveJwtDecoder jwtDecoder() throws IOException, InvalidKeyException {
        return WebClient.create().get().uri(URI.create(keyUri)).header("Authorization", (new BasicAuthHead(clientId, password)).getHeaderValue())
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(JwtPublicKey.class))
                .map(jwtPublicKey -> parsePublicKey(jwtPublicKey.getValue()))
                .map(NimbusReactiveJwtDecoder::new).block();
    }

    private RSAPublicKey parsePublicKey(String keyValue) {
        PemReader pemReader = new PemReader(new StringReader(keyValue));
        PemObject pem = null;
        try {
            pem = pemReader.readPemObject();
            return new RSAPublicKeyImpl(pem.getContent());
        } catch (IOException | InvalidKeyException e) {
            LOGGER.error("Unable to parse public key", e);
        }
        return null;
    }

}
