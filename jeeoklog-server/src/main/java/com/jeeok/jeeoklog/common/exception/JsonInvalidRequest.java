package com.jeeok.jeeoklog.common.exception;

import com.jeeok.jeeoklog.common.json.ErrorCode;
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
