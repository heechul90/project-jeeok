package com.jeeok.jeeokmember.common.exception;

import org.springframework.http.HttpStatus;

public abstract class EntityNotFoundException extends CommonException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
