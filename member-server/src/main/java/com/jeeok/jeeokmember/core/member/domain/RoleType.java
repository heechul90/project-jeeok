package com.jeeok.jeeokmember.core.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum RoleType {

    ROLE_USER("일반유저", List.of("ROLE_USER")),
    ROLE_DELIVERYMAN("배달원", List.of("ROLE_DELIVERYMAN")),
    ROLE_MANAGER("매니저", List.of("ROLE_MANAGER")),
    ROLE_ADMIN("관리자", List.of("ROLE_USER", "ROLE_DELIVERYMAN", "ROLE_MANAGER", "ROLE_ADMIN"));

    private final String typeName;
    private List<String> roles;
}
