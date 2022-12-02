package com.jeeok.jeeokshop.core.notification.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KafkaSendOrderDto {

    private Long orderId;
    private Long memberId;
    private List<Long> itemIds;

}
