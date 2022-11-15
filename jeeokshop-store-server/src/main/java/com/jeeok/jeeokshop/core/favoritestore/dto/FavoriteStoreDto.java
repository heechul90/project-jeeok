package com.jeeok.jeeokshop.core.favoritestore.dto;

import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FavoriteStoreDto {

    private Long favoriteStoreId;

    private Long storeId;
    private String storeName;

    public FavoriteStoreDto(FavoriteStore favoriteStore) {
        this.favoriteStoreId = favoriteStore.getId();
        this.storeId = favoriteStore.getStore().getId();
        this.storeName = favoriteStore.getStore().getName();
    }
}
