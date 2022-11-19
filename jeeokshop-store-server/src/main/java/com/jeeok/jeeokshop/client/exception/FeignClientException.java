package com.jeeok.jeeokshop.client.exception;

import com.jeeok.jeeokshop.common.exception.CommonException;
import org.springframework.http.HttpStatus;

public class FeignClientException extends CommonException {

    public FeignClientException(String message) {
        super(message);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
