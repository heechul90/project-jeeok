package com.jeeok.jeeokshop.core.deliveryRider.controller.request;

import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.deliveryRider.dto.SaveDeliveryRiderParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RegisterMyDeliveryRequest {

    private Long deliveryId;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();


        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public SaveDeliveryRiderParam toParam() {
        return SaveDeliveryRiderParam.builder()
                .deliveryId(this.deliveryId)
                .build();
    }
}
