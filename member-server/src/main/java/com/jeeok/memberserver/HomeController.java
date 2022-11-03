package com.jeeok.memberserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RefreshScope
public class HomeController {

    @Value("${msa.test}")
    private String str;

    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        log.info("port = {}", request.getLocalPort());
        return str + request.getLocalPort();
    }

    @GetMapping("/test2")
    public String test2(HttpServletRequest request) {
        log.info("port = {}", request.getLocalPort());
        return str + request.getLocalPort() + "22222";
    }
}
