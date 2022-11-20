package com.jeeok.jeeokshop.core.item.controller.request;

import com.jeeok.jeeokshop.common.entity.Photo;
import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.item.dto.SaveItemParam;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RegisterItemRequest {

    @NotBlank
    private String itemName;

    private int itemPrice;
    private int stockQuantity;

    private String photoPath;
    private String photoName;

    @NotNull
    private Long categoryId;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public SaveItemParam toParam() {
        return SaveItemParam.builder()
                .name(this.itemName)
                .price(this.itemPrice)
                .stockQuantity(this.stockQuantity)
                .photo(new Photo(this.photoPath, this.photoName))
                .build();
    }
}
