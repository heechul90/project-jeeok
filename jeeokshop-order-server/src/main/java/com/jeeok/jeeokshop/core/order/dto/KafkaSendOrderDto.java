package com.jeeok.jeeokshop.core.order.dto;

import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KafkaSendOrderDto {

    private Long orderId;
    private Long memberId;
    private List<Long> itemIds;

    public static KafkaSendOrderDto createPrimitiveField(Order order) {
        return KafkaSendOrderDto.builder()
                .orderId(order.getId())
                .memberId(order.getMemberId())
                .itemIds(
                        order.getOrderItems().stream()
                        .map(OrderItem::getItemId)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
