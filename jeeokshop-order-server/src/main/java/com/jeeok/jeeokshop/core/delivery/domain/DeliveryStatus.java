package com.jeeok.jeeokshop.core.delivery.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    READY("배송준비"),
    DELIVERY("배송중"),
    COMPLETE("배송완료");

    private final String description;
}
