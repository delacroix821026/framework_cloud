package com.newland.financial.p2p;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        System.out.println("--------------GOOGLE_APPLICATION_CREDENTIALS------------"
                + System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }
}
