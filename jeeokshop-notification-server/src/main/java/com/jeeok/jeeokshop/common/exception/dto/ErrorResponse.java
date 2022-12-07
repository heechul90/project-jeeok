package com.jeeok.jeeokshop.common.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private int status;
    private String code;
    //private List<FieldError> errors;

    private static final String DEFAULT_ERROR_MESSAGE = "ERROR";
}
