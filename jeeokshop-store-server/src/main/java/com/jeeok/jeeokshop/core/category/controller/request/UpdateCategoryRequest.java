package com.jeeok.jeeokshop.core.category.controller.request;

import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.category.dto.UpdateCategoryParam;
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
public class UpdateCategoryRequest {

    @NotBlank
    private String categoryName;
    @NotNull
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
