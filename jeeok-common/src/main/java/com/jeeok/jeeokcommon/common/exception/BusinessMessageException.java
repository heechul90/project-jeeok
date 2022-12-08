package com.jeeok.jeeokcommon.common.exception;

import com.jeeok.jeeokshop.common.exception.dto.ErrorCode;

public class BusinessMessageException extends BusinessException {

    /**
     * 사용자에게 표시될 메시지와 상태코드 400 을 넘긴다.
     */
    public BusinessMessageException(String customMessage) {
        super(ErrorCode.BUSINESS_CUSTOM_MESSAGE, customMessage);
    }
}
