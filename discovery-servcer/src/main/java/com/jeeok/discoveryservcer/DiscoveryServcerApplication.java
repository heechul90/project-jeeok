package com.jeeok.discoveryservcer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServcerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServcerApplication.class, args);
    }

}
