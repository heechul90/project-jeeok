package com.jeeok.jeeokshop.core.item.controller.request;

import com.jeeok.jeeokshop.common.entity.Photo;
import com.jeeok.jeeokshop.common.entity.Yn;
import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.item.dto.UpdateItemParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateItemRequest {

    private String itemName;
    private Yn salesYn;
    private int itemPrice;
    private String photoPath;
    private String photoName;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public UpdateItemParam toParam() {
        return UpdateItemParam.builder()
                .name(this.itemName)
                .yn(this.salesYn)
                .price(this.itemPrice)
                .photo(new Photo(this.photoPath, this.photoName))
                .build();
    }
}
