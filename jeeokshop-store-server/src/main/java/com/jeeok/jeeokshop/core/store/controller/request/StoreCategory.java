package com.jeeok.jeeokshop.core.store.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoreCategory {

    private String categoryName;
    private Integer categoryOrder;
}
