package com.fw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.cloud.bootstrap.encrypt.RsaProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.encryption.KeyStoreTextEncryptorLocator;
import org.springframework.cloud.config.server.encryption.TextEncryptorLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.rsa.crypto.RsaAlgorithm;
import org.springframework.security.rsa.crypto.RsaSecretEncryptor;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableConfigServer
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*@Bean
    public RsaProperties getRsaProperties() throws Exception {
        this.getClass().getClassLoader().loadClass("org.springframework.security.rsa.crypto.RsaSecretEncryptor");
        return new RsaProperties();
    }

    @Autowired
    private KeyProperties key;

    @Autowired
    private RsaProperties rsaProperties;

    @Bean
    @ConditionalOnMissingBean
    public TextEncryptorLocator textEncryptorLocator() {
        KeyProperties.KeyStore keyStore = this.key.getKeyStore();
        KeyStoreTextEncryptorLocator locator = new KeyStoreTextEncryptorLocator(
                new KeyStoreKeyFactory(keyStore.getLocation(), keyStore.getPassword().toCharArray()),
                keyStore.getSecret(), keyStore.getAlias());
        RsaAlgorithm algorithm = this.rsaProperties.getAlgorithm();
        locator.setRsaAlgorithm(algorithm);
        locator.setSalt(this.rsaProperties.getSalt());
        locator.setStrong(this.rsaProperties.isStrong());
        return locator;
    }*/
}
