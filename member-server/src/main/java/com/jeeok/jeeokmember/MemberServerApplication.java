package com.jeeok.jeeokmember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = {"com.jeeok.jeeokcommon.common"})
//@EntityScan(basePackages = {"com.jeeok.jeeokcommon.common.entity"})
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class MemberServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberServerApplication.class, args);
    }

}
