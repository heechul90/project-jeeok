package com.jeeok.jeeokshop.core.order.controller.request;

import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.order.dto.OrderParam;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull
    private List<OrderRequest.Item> items;

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Item {
        @NotNull
        private Long itemId;

        private int orderPrice;
        private int orderCount;
    }

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public OrderParam toParam() {
        return OrderParam.builder()
                .items(this.items.stream()
                        .map(item -> OrderParam.Item.builder()
                                .itemId(item.getItemId())
                                .price(item.getOrderPrice())
                                .count(item.getOrderCount())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}
