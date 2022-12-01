package com.jeeok.jeeokshop.common.json;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String fieldName;
    private String errorMessage;
}
