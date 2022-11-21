package com.jeeok.jeeokshop.core.deliveryRider.controller.request;

import com.jeeok.jeeokshop.common.entity.PhoneNumber;
import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.deliveryRider.dto.UpdateDeliveryRiderParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EditMyDeliveryRiderRequest {

    private String riderName;
    private String phoneNumber;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();


        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public UpdateDeliveryRiderParam toParam() {
        return UpdateDeliveryRiderParam.builder()
                .riderName(this.riderName)
                .phoneNumber(new PhoneNumber(this.phoneNumber.substring(0, 3), this.phoneNumber.substring(3, 7), this.phoneNumber.substring(7, 11)))
                .build();
    }
}
