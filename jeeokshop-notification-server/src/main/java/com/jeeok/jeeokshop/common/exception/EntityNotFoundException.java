package com.jeeok.jeeokshop.common.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message, List<ErrorCode> errorCode) {
        super(message, errorCode);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
