package com.jeeok.jeeokshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class JeeokshopStoreServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeeokshopStoreServerApplication.class, args);
    }

}
