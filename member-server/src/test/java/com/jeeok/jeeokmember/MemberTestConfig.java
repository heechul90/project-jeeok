package com.jeeok.jeeokmember;

import com.jeeok.jeeokmember.core.member.repository.MemberQueryRepository;
import com.jeeok.jeeokmember.core.member.repository.MemberRepository;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class MemberTestConfig {

    @PersistenceContext EntityManager em;

    @Autowired MemberRepository memberRepository;

    @Bean
    public MemberService memberService() {
        return new MemberService(memberQueryRepository(), memberRepository);
    }

    @Bean
    public MemberQueryRepository memberQueryRepository() {
        return new MemberQueryRepository(em);
    }

}
