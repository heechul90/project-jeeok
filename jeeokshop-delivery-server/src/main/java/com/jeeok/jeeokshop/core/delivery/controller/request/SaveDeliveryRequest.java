package com.jeeok.jeeokshop.core.delivery.controller.request;

import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveDeliveryRequest {

    private Long memberId;
    private Long orderId;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public Delivery toDelivery() {
        return Delivery.createDelivery()
                .memberId(this.memberId)
                .orderId(this.orderId)
                .build();
    }
}
