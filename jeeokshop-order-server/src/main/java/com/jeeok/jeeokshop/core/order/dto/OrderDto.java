package com.jeeok.jeeokshop.core.order.dto;

import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.orderItem.dto.OrderItemDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long orderId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus().getDescription();

        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
    }
}
