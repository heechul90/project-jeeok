package com.jeeok.jeeokshop.core.category.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.dto.CategorySearchCondition;
import com.jeeok.jeeokshop.core.category.dto.UpdateCategoryParam;
import com.jeeok.jeeokshop.core.category.repository.CategoryQueryRepository;
import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
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

    private final CategoryQueryRepository categoryQueryRepository;
    private final CategoryRepository categoryRepository;

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
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * 카테고리 수정
     */
    public void updateCategory(Long categoryId, UpdateCategoryParam param) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFound(CATEGORY, categoryId.toString()));
        findCategory.updateCategory(param);
    }

    /**
     * 카테고리 삭제
     */
    public void deleteCategory(Long categoryId) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFound(CATEGORY, categoryId.toString()));
        categoryRepository.delete(findCategory);
    }
}
