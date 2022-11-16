package com.jeeok.jeeokmember;

import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
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
            Member springMember1 = getMember(
                    "spring1",
                    "1234",
                    "스프링1",
                    RoleType.ROLE_USER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "2397", "6591")
            );
            Member springMember2 = getMember(
                    "spring2",
                    "1234",
                    "스프링2",
                    RoleType.ROLE_USER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "4422", "6242")
            );
            Member managerMember1 = getMember(
                    "manager1",
                    "1234",
                    "교촌치킨 매니저",
                    RoleType.ROLE_MANAGER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "5678", "2345")
            );
            Member managerMember2 = getMember(
                    "manager2",
                    "1234",
                    "BHC치킨 매니저",
                    RoleType.ROLE_MANAGER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "3432", "2356")
            );
            Member adminMember = getMember(
                    "admin",
                    "1234",
                    "관리자",
                    RoleType.ROLE_ADMIN,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "1111", "2222")
            );
            em.persist(springMember1);
            em.persist(springMember2);
            em.persist(managerMember1);
            em.persist(managerMember2);
            em.persist(adminMember);
        }
    }
}
