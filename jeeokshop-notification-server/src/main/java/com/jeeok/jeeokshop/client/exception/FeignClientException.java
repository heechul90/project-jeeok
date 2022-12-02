package com.jeeok.jeeokshop.client.exception;

import com.jeeok.jeeokshop.common.exception.BusinessException;
import com.jeeok.jeeokshop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.List;

public class FeignClientException extends BusinessException {

    public static final String MESSAGE = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();

    public FeignClientException(ErrorCode errorCode) {
        super(MESSAGE, List.of(errorCode));
    }

    public FeignClientException(String message, ErrorCode errorCodes) {
        super(message, List.of(errorCodes));
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
