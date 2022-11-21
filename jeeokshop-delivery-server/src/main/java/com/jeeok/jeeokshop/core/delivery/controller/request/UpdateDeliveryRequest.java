package com.jeeok.jeeokshop.core.delivery.controller.request;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.delivery.dto.UpdateDeliveryParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateDeliveryRequest {

    private String zipcode;
    private String address;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public UpdateDeliveryParam toParam() {
        return UpdateDeliveryParam.builder()
                .address(new Address(this.zipcode, this.address))
                .build();
    }
}
