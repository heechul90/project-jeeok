package com.jeeok.jeeokshop.common.json;

import com.jeeok.jeeokshop.common.exception.ErrorCode;
import com.jeeok.jeeokshop.common.exception.InvalidValueException;

import java.util.List;

public class JsonInvalidRequest extends InvalidValueException {

    public static final String MESSAGE = "Json Bad Request";

    public JsonInvalidRequest(List<ErrorCode> errorCodes) {
        super(MESSAGE, errorCodes);
    }
}
