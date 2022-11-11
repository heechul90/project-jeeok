package com.jeeok.jeeokshop.core.category.controller.request;

import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.category.dto.UpdateCategoryParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateCategoryRequest {

    private String categoryName;
    private Integer categoryOrder;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public UpdateCategoryParam toParam() {
        return UpdateCategoryParam.builder()
                .name(this.categoryName)
                .order(this.categoryOrder)
                .build();
    }
}
