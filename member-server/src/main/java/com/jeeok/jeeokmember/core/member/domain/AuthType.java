package com.jeeok.jeeokmember.core.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthType {

    NAVER("네이버"),
    KAKAO("카카오"),
    GOOGLE("구글"),
    GITHUB("깃허브"),
    JEEOK("JEEOK");

    private final String typeName;
}
