package com.jeeok.jeeokcommon.common.exception;

import com.jeeok.jeeokshop.common.exception.dto.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
