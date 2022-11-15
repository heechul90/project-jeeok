package com.jeeok.jeeokshop.core.favoritestore.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import com.jeeok.jeeokshop.core.favoritestore.dto.FavoriteStoreSearchCondition;
import com.jeeok.jeeokshop.core.favoritestore.repository.FavoriteStoreQueryRepository;
import com.jeeok.jeeokshop.core.favoritestore.repository.FavoriteStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteStoreService {

    public static final String FAVORITE_STORE = "FavoriteStore";
    private final FavoriteStoreQueryRepository favoriteStoreQueryRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;

    /**
     * 호감 스토어 목록 조회
     */
    public Page<FavoriteStore> findFavoriteStores(FavoriteStoreSearchCondition condition, Pageable pageable) {
        return favoriteStoreQueryRepository.findFavoriteStores(condition, pageable);
    }

    /**
     * 호감 스토어 단건 조회
     */
    public FavoriteStore findFavoriteStore(Long favoriteStoreId) {
        return favoriteStoreRepository.findById(favoriteStoreId)
                .orElseThrow(() -> new EntityNotFound(FAVORITE_STORE, favoriteStoreId.toString()));
    }

    /**
     * 호감 스토어 저장
     */

    /**
     * 호감 스토어 수정
     */

    /**
     * 호감 스토어 삭제
     */
    public void deleteFavoriteStore(Long favoriteStoreId) {
        FavoriteStore findFavoriteStore = favoriteStoreRepository.findById(favoriteStoreId)
                .orElseThrow(() -> new EntityNotFound(FAVORITE_STORE, favoriteStoreId.toString()));
        favoriteStoreRepository.delete(findFavoriteStore);
    }
}
