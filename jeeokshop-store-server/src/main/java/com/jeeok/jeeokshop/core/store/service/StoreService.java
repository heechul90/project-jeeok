package com.jeeok.jeeokshop.core.store.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.dto.UpdateCategoryParam;
import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    public static final String STORE = "Store";
    public static final String CATEGORY = "Category";

    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

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
                .orElseThrow(() -> new EntityNotFound(STORE, storeId));
    }

    /**
     * 스토어 저장
     */
    @Transactional
    public Store saveStore(SaveStoreParam param) {
        List<Category> categories = param.getStoreCategoryParams().stream()
                .map(category -> Category.createCategory()
                        .name(category.getName())
                        .order(category.getOrder())
                        .build()
                ).collect(Collectors.toList());

        Store store = Store.createStore()
                .name(param.getName())
                .businessHours(param.getBusinessHours())
                .phoneNumber(param.getPhoneNumber())
                .address(param.getAddress())
                .memberId(param.getMemberId())
                .categories(categories)
                .build();
        return storeRepository.save(store);
    }

    /**
     * 스토어 수정
     */
    @Transactional
    public void updateStore(Long storeId, UpdateStoreParam param) {
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFound(STORE, storeId));
        findStore.updateStore(param);

        //categoryId 가 있으면 업데이트 없으면 생성
        param.getStoreCategoryParams().forEach(category -> {
            if (category.getCategoryId() != null) {
                Category findCategory = categoryRepository.findById(category.getCategoryId())
                        .orElseThrow(() -> new EntityNotFound(CATEGORY, category.getCategoryId()));
                findCategory.updateCategory(
                        UpdateCategoryParam.builder()
                                .name(category.getName())
                                .order(category.getOrder())
                                .build()
                );
            } else {
                categoryRepository.save(
                        Category.createCategory()
                                .name(category.getName())
                                .order(category.getOrder())
                                .store(findStore)
                                .build()
                );
            }
        });
    }

    /**
     * 스토어 삭제
     */
    @Transactional
    public void deleteStore(Long storeId) {
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFound(STORE, storeId));
        findStore.deleteStore();
    }
}
