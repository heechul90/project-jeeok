package com.jeeok.jeeokshop.core.category.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.category.controller.request.SaveCategoryRequest;
import com.jeeok.jeeokshop.core.category.controller.request.UpdateCategoryRequest;
import com.jeeok.jeeokshop.core.category.controller.response.SaveCategoryResponse;
import com.jeeok.jeeokshop.core.category.controller.response.UpdateCategoryResponse;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.dto.CategoryDto;
import com.jeeok.jeeokshop.core.category.dto.CategorySearchCondition;
import com.jeeok.jeeokshop.core.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록 조회
     */
    @GetMapping
    public JsonResult findCategories(CategorySearchCondition condition, @PageableDefault(size = 10) Pageable pageable) {
        Page<Category> content = categoryService.findCategories(condition, pageable);
        List<CategoryDto> categories = content.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
        return JsonResult.OK(categories);
    }

    /**
     * 카테고리 단건 조회
     */
    @GetMapping("/{categoryId}")
    public JsonResult findCategory(@PathVariable("categoryId") Long categoryId) {
        Category findCategory = categoryService.findCategory(categoryId);
        CategoryDto category = new CategoryDto(findCategory);
        return JsonResult.OK(category);
    }

    /**
     * 카테고리 저장
     */
    @PostMapping
    public JsonResult saveCategory(@RequestBody @Validated SaveCategoryRequest request) {

        //validate
        request.validate();

        Category savedCategory = categoryService.saveCategory(request.getStoreId(), request.toParam());

        return JsonResult.OK(new SaveCategoryResponse(savedCategory.getId()));
    }

    /**
     * 카테고리 수정
     */
    @PutMapping("/{categoryId}")
    public JsonResult updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody @Validated UpdateCategoryRequest request) {

        //validate
        request.validate();

        categoryService.updateCategory(categoryId, request.toParam());
        Category updatedCategory = categoryService.findCategory(categoryId);

        return JsonResult.OK(new UpdateCategoryResponse(updatedCategory.getId()));

    }

    /**
     * 카테고리 삭제
     */
    @DeleteMapping("/{categoryId}")
    public JsonResult deleteCategory(@PathVariable("categoryId") Long categoryId) {

        categoryService.deleteCategory(categoryId);

        return JsonResult.OK();
    }


}
