package com.jeeok.jeeokshop.common.exception;

import com.jeeok.jeeokshop.common.exception.dto.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.List;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
