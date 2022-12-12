package com.jeeok.jeeokshop.client.exception;

import com.jeeok.jeeokshop.common.exception.BusinessException;
import com.jeeok.jeeokshop.common.exception.dto.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.List;

public class FeignClientException extends BusinessException {

    public FeignClientException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
