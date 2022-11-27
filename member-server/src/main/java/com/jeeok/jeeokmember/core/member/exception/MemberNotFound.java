package com.jeeok.jeeokmember.core.member.exception;

import com.jeeok.jeeokmember.common.exception.EntityNotFoundException;

public class MemberNotFound extends EntityNotFoundException {

    public MemberNotFound(Long notFoundMemberId) {
        super("존재하지 않는 회원입니다. 회원 고유번호=" + notFoundMemberId);
    }
}
