package com.jeeok.jeeokshop.core.delivery.exception;

import com.jeeok.jeeokshop.common.exception.CommonException;
import org.springframework.http.HttpStatus;

public class OrderNotFound extends CommonException {

    public OrderNotFound() {
        super("주문내역이 없습니다.");
    }

    public OrderNotFound(String message) {
        super(message);
    }

    public OrderNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
