package com.jeeok.jeeokshop.core.store.exception;

import com.jeeok.jeeokshop.common.exception.CommonException;
import org.springframework.http.HttpStatus;

public class MemberNotEqual extends CommonException {

    public MemberNotEqual() {
        super("작성자만 수정할 수 있습니다.");
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
