package com.jeeok.jeeokshop.core.store.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateStoreResponse {

    private Long updatedStoreId;
}
