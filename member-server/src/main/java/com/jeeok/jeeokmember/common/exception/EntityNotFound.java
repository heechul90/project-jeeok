package com.jeeok.jeeokmember.common.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFound extends CommonException {

    public EntityNotFound(String entity, Long id) {
        super("존재하지 않는 " + entity + "입니다. id=" + id);
    }

    public EntityNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
