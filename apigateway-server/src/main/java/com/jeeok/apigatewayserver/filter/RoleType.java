package com.jeeok.apigatewayserver.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum RoleType {

    ROLE_USER("유저", List.of("ROLE_USER")),
    ROLE_RIDER("배달원", List.of("ROLE_RIDER")),
    ROLE_MANAGER("매니저", List.of("ROLE_MANAGER")),
    ROLE_ADMIN("관리자", List.of("ROLE_USER", "ROLE_RIDER", "ROLE_MANAGER", "ROLE_ADMIN"));

    private final String typeName;
    private List<String> roles;
}
