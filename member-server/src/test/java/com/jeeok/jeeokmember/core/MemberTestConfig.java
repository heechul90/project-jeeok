package com.jeeok.jeeokmember.core;

import com.jeeok.jeeokmember.core.repository.MemberQueryRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class MemberTestConfig {

    @PersistenceContext
    EntityManager em;

    @Bean
    public MemberQueryRepository memberQueryRepository() {
        return new MemberQueryRepository(em);
    }
}
