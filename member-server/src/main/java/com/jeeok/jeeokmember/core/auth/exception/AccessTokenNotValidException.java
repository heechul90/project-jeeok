package com.jeeok.jeeokmember.core.auth.exception;

import com.jeeok.jeeokmember.common.exception.CommonException;
import org.springframework.http.HttpStatus;

public class AccessTokenNotValidException extends CommonException {

    public AccessTokenNotValidException(String message) {
        super(message);
    }

    public AccessTokenNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
