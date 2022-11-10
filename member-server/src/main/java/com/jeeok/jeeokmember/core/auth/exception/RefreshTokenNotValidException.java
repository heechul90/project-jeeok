package com.jeeok.jeeokmember.core.auth.exception;

import com.jeeok.jeeokmember.common.json.JsonResult;
import lombok.Getter;

@Getter
public class RefreshTokenNotValidException extends RuntimeException {

    private JsonResult jsonResult;

    public RefreshTokenNotValidException(String message) {
        this.jsonResult = JsonResult.ERROR(message);
    }
}
