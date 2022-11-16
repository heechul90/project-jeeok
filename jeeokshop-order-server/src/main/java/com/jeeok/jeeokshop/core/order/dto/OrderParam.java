package com.jeeok.jeeokshop.core.order.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderParam {

    private List<OrderParam.Item> items;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Item {
        private Long itemId;
        private int price;
        private int count;
    }
}
