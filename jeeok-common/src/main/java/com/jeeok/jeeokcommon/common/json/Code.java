package com.jeeok.jeeokcommon.common.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Code {

    SUCCESS("성공"),
    ERROR("실패");

    private final String codeName;
}
