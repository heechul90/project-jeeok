package com.jeeok.jeeokmember.common.exception;

import com.jeeok.jeeokmember.common.json.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.List;

public class JsonInvalidRequest extends CommonException {

    public static final String MESSAGE = HttpStatus.BAD_REQUEST.getReasonPhrase();

    public JsonInvalidRequest() {
        super(MESSAGE);
    }

    public JsonInvalidRequest(List<ErrorCode> errorCodes) {
        super(MESSAGE);
        addError(errorCodes);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
