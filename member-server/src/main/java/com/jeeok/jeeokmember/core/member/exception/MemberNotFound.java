package com.jeeok.jeeokmember.core.member.exception;

import com.jeeok.jeeokmember.common.exception.CommonException;
import org.springframework.http.HttpStatus;

public class MemberNotFound extends CommonException {

    public MemberNotFound(Long notFoundMemberId) {
        super("존재하지 않는 회원입니다. 회원 고유번호=" + notFoundMemberId);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
