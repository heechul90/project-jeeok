package com.jeeok.jeeokshop.core.category.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.dto.CategorySearchCondition;
import com.jeeok.jeeokshop.core.category.dto.SaveCategoryParam;
import com.jeeok.jeeokshop.core.category.dto.UpdateCategoryParam;
import com.jeeok.jeeokshop.core.category.repository.CategoryQueryRepository;
import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
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
public class CategoryService {

    public static final String CATEGORY = "Category";
    public static final String STORE = "Store";

    private final CategoryQueryRepository categoryQueryRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    /**
     * 카테고리 목록 조회
     */
    public Page<Category> findCategories(CategorySearchCondition condition, Pageable pageable) {
        return categoryQueryRepository.findCategories(condition, pageable);
    }

    /**
     * 카테고리 단건 조회
     */
    public Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFound(CATEGORY, categoryId.toString()));
    }

    /**
     * 카테고리 저장
     */
    @Transactional
    public Category saveCategory(Long storeId, SaveCategoryParam param) {

        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFound(STORE, storeId.toString()));
        Category category = Category.createCategory()
                .name(param.getName())
                .order(param.getOrder())
                .store(findStore)
                .build();
        return categoryRepository.save(category);
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public void updateCategory(Long categoryId, UpdateCategoryParam param) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFound(CATEGORY, categoryId.toString()));
        findCategory.updateCategory(param);
    }

    /**
     * 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFound(CATEGORY, categoryId.toString()));
        categoryRepository.delete(findCategory);
    }
}
