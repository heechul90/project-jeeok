package com.jeeok.jeeokshop.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BusinessException extends RuntimeException {

    private List<ErrorCode> errorCodes = new ArrayList<>();

    public BusinessException(String message, List<ErrorCode> errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }

    public abstract HttpStatus httpStatus();
}
