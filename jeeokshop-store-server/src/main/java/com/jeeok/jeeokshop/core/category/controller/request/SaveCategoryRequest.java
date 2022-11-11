package com.jeeok.jeeokshop.core.category.controller.request;

import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.category.dto.SaveCategoryParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveCategoryRequest {

    private String categoryName;
    private Integer categoryOrder;
    private Long storeId;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public SaveCategoryParam toParam() {
        return SaveCategoryParam.builder()
                .name(this.categoryName)
                .order(this.categoryOrder)
                .build();
    }
}
