package com.jeeok.jeeokshop.core.store.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.SaveStoreParam;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import com.jeeok.jeeokshop.core.store.dto.UpdateStoreParam;
import com.jeeok.jeeokshop.core.store.repository.StoreQueryRepository;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    public static final String STORE = "Store";

    private StoreQueryRepository storeQueryRepository;
    private StoreRepository storeRepository;

    /**
     * 스토어 목록 조회
     */
    public Page<Store> findStores(StoreSearchCondition condition, Pageable pageable) {
        return storeQueryRepository.findStores(condition, pageable);
    }

    /**
     * 스토어 단건 조회
     */
    public Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFound(STORE, storeId.toString()));
    }

    /**
     * 스토어 저장
     */
    @Transactional
    public Store saveStore(SaveStoreParam param) {
        Store store = Store.createStore()
                .name(param.getName())
                .businessHours(param.getBusinessHours())
                .phoneNumber(param.getPhoneNumber())
                .address(param.getAddress())
                .memberId(param.getMemberId())
                .categories(
                        param.getStoreCategories().stream()
                                .map(storeCategoryParam -> Category.createCategory()
                                        .name(storeCategoryParam.getName())
                                        .order(storeCategoryParam.getOrder())
                                        .build()
                                ).collect(Collectors.toList())
                )
                .build();
        return storeRepository.save(store);
    }

    /**
     * 스토어 수정
     */
    @Transactional
    public void updateStore(Long storeId, UpdateStoreParam param) {
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFound(STORE, storeId.toString()));
        findStore.updateStore(param);
    }

    /**
     * 스토어 삭제
     */
    @Transactional
    public void deleteStore(Long storeId) {
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFound(STORE, storeId.toString()));

        storeRepository.delete(findStore);
    }
}
