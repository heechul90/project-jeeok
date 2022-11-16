package com.jeeok.jeeokshop.core.favoritestore.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AddFavoriteStoreResponse {

    private Long addedFavoriteStoreId;
}
