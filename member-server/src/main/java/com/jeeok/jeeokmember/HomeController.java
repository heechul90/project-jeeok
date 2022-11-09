package com.jeeok.jeeokmember;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeController {

    /*@Value("${msa.test}")
    private String TEST;

    @GetMapping("/test")
    public String test() {
        return "hello " + TEST;
    }*/
}
