package com.jeeok.jeeokshop.common.json;

import com.jeeok.jeeokshop.common.exception.InvalidValueException;
import com.jeeok.jeeokshop.common.exception.dto.ErrorCode;

import java.util.List;

public class JsonInvalidRequest extends InvalidValueException {

    public JsonInvalidRequest(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
