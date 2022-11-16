package com.jeeok.jeeokshop.core.orderItem.dto;

import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Long orderItemId;
    private Long itemId;
    private int orderPrice;
    private int orderCount;

    public OrderItemDto(OrderItem orderItem) {
        this.orderItemId = orderItem.getId();
        this.itemId = orderItem.getItemId();
        this.orderPrice = orderItem.getPrice();
        this.orderCount = orderItem.getCount();
    }
}
