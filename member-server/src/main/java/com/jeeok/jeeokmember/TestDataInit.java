package com.jeeok.jeeokmember;

import com.jeeok.jeeokmember.core.domain.AuthType;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.domain.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final InitService initService;

    @PostConstruct
    public void init() {

        //멤버
        initService.memberInit();

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        @PersistenceContext public final EntityManager em;

        private static Member getMember(String email, String password, String name, RoleType roleType, AuthType authType, PhoneNumber phoneNumber) {
            return Member.createMember()
                    .email(email)
                    .password(password)
                    .name(name)
                    .roleType(roleType)
                    .authType(authType)
                    .phoneNumber(phoneNumber)
                    .build();
        }

        public void memberInit() {
            Member springMember = getMember(
                    "spring",
                    "1234",
                    "스프링",
                    RoleType.ROLE_USER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "1111", "2222")
            );
            Member adminMember = getMember(
                    "admin",
                    "1234",
                    "관리자",
                    RoleType.ROLE_ADMIN,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "1111", "2222")
            );
            em.persist(springMember);
            em.persist(adminMember);
        }
    }
}
