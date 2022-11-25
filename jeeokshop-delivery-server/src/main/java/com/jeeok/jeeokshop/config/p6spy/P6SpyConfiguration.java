package com.jeeok.jeeokshop.config.p6spy;

import com.p6spy.engine.spy.P6SpyOptions;

import javax.annotation.PostConstruct;

//@Configuration
public class P6SpyConfiguration {

    @PostConstruct
    public void setMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(CustomP6SpySqlFormat.class.getName());
    }
}
