package com.jeeok.jeeokcommon.common.json;


import com.jeeok.jeeokcommon.common.exception.InvalidValueException;
import com.jeeok.jeeokcommon.common.exception.dto.ErrorCode;

public class JsonInvalidRequest extends InvalidValueException {

    public JsonInvalidRequest(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
