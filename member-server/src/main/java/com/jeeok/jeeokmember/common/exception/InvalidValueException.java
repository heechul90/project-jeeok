package com.jeeok.jeeokmember.common.exception;

import com.jeeok.jeeokmember.common.json.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidValueException extends CommonException {

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public void addError(List<ErrorCode> errorCodes) {
        super.addError(errorCodes);
    }
}
