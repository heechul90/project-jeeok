package com.jeeok.jeeokshop.core.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    ORDER("주문"),
    CANCEL("주문취소");

    private final String description;
}
