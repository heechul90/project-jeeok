package com.jeeok.jeeokcommon.common.exception;

import com.jeeok.jeeokcommon.common.exception.dto.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private ErrorCode errorCode;
    private String customMessage;

    /**
     * 사용자 정의 메시지를 받아 처리하는 경우
     */
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    /**
     * 사전 정의된 에러코드 객체를 넘기는 경우
     */
    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 사전 정의된 에러코드의 메시지를 서버에 남기고 에러코드 객체를 리턴하는 경우
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
