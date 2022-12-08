package com.jeeok.jeeokmember;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.jeeok.jeeokcommon.common"})
@EntityScan(basePackages = {"com.jeeok.jeeokcommon.common.entity"})
@SpringBootTest
class MemberServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
