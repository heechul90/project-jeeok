package com.jeeok.jeeokmember.core.member.exception;

import com.jeeok.jeeokmember.common.exception.CommonException;
import org.springframework.http.HttpStatus;

public class MemberAlreadyExist extends CommonException {

    public static final String MESSAGE = "회원이 이미 존재합니다.";

    public MemberAlreadyExist() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
