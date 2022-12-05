package com.jeeok.jeeokshop.core.delivery.dto;

import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KafkaSendDeliveryDto {

    private Long deliveryId;
    private Long orderId;
    private Long memberId;
    private DeliveryStatus status;

    public static KafkaSendDeliveryDto createPrimitiveField(Delivery delivery) {
        return KafkaSendDeliveryDto.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .memberId(delivery.getMemberId())
                .status(delivery.getStatus())
                .build();
    }
}
