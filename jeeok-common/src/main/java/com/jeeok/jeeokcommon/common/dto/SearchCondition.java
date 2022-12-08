package com.jeeok.jeeokcommon.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchCondition {

    NAME("이름"),
    TITLE("제목"),
    CONTENT("내용");

    private final String codeName;
}
