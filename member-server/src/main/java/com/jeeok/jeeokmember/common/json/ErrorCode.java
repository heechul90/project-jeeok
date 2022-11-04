package com.jeeok.jeeokmember.common.json;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ErrorCode {

    private String fieldName;
    private String errorCode;
    private Object[] arguments;
}
