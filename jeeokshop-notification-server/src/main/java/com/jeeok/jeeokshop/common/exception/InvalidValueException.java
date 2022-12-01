package com.jeeok.jeeokshop.common.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String message, List<ErrorCode> errorCodes) {
        super(message, errorCodes);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
