package com.jeeok.jeeokshop.core.category.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.dto.SaveCategoryParam;
import com.jeeok.jeeokshop.core.category.dto.UpdateCategoryParam;
import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CategoryServiceTest extends MockTest {

    //CREATE_CATEGORY
    public static final Long CATEGORY_ID_1 = 1L;
    public static final String NAME = "category_name";
    public static final int ORDER = 1;
    public static final long STORE_ID_1 = 1L;

    //UPDATE_CATEGORY
    public static final String UPDATE_NAME = "update_name";
    public static final int UPDATE_ORDER = 2;

    //ERROR_MESSAGE
    public static final String CATEGORY = "Category";
    public static final Long NOT_FOUND_CATEGORY_ID = 0L;
    public static final String STORE = "Store";
    public static final Long NOT_FOUND_STORE_ID = 0L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    @Mock protected CategoryRepository categoryRepository;
    @Mock protected StoreRepository storeRepository;
    @Mock protected Store store;
    @InjectMocks protected CategoryService categoryService;

    private Category getCategory(String name, int order) {
        return Category.createCategory()
                .name(name)
                .order(order)
                .build();
    }

    Category category;

    @BeforeEach
    void beforeEach() {
        category = getCategory(NAME, ORDER);
    }

    @Nested
    class SuccessfulTest {
        @Test
        @DisplayName("카테고리 단건 조회")
        void findCategory() {
            //given
            given(categoryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(category));

            //when
            Category findCategory = categoryService.findCategory(CATEGORY_ID_1);

            //then
            assertThat(findCategory.getName()).isEqualTo(NAME);
            assertThat(findCategory.getOrder()).isEqualTo(ORDER);

            //verify
            verify(categoryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("카테고리 저장")
        void saveCategory() {
            //given
            given(categoryRepository.save(any(Category.class))).willReturn(category);
            given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));

            SaveCategoryParam param = SaveCategoryParam.builder()
                    .name(NAME)
                    .order(ORDER)
                    .storeId(STORE_ID_1)
                    .build();

            //when
            Category savedCategory = categoryService.saveCategory(1L, param);

            //then
            assertThat(savedCategory.getName()).isEqualTo(NAME);
            assertThat(savedCategory.getOrder()).isEqualTo(ORDER);

            //verify
            verify(storeRepository, times(1)).findById(any(Long.class));
            verify(categoryRepository, times(1)).save(any(Category.class));
        }

        @Test
        @DisplayName("카테고리 수정")
        void updateCategory() {
            //given
            given(categoryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(category));

            UpdateCategoryParam param = UpdateCategoryParam.builder()
                    .name(UPDATE_NAME)
                    .order(UPDATE_ORDER)
                    .build();

            //when
            categoryService.updateCategory(CATEGORY_ID_1, param);

            //then
            assertThat(category.getName()).isEqualTo(UPDATE_NAME);
            assertThat(category.getOrder()).isEqualTo(UPDATE_ORDER);

            //verify
            verify(categoryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("카테고리 삭제")
        void deleteCategory() {
            //given
            given(categoryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(category));

            //when
            categoryService.deleteCategory(CATEGORY_ID_1);

            //then

            //verify
            verify(categoryRepository, times(1)).findById(any(Long.class));
            verify(categoryRepository, times(1)).delete(any(Category.class));
        }
    }

    @Nested
    class EntityNotFoundTest {
        @Test
        @DisplayName("카테고리 단건 조회_예외")
        void findCategory_exception() {
            //given
            given(categoryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(CATEGORY, NOT_FOUND_CATEGORY_ID));

            //expected
            assertThatThrownBy(() -> categoryService.findCategory(NOT_FOUND_CATEGORY_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + CATEGORY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_CATEGORY_ID);

            //verify
            verify(categoryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("카테고리 저장_예외")
        void saveCategory_exception() {
            //given
            given(storeRepository.findById(any(Long.class))).willThrow(new EntityNotFound(STORE, NOT_FOUND_STORE_ID));

            //expected
            assertThatThrownBy(() -> categoryService.saveCategory(NOT_FOUND_STORE_ID, any(SaveCategoryParam.class)))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + STORE)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_STORE_ID);

            //verify
            verify(storeRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("카테고리 수정_예외")
        void updateCategory_exception() {
            //given
            given(categoryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(CATEGORY, NOT_FOUND_CATEGORY_ID));

            //expected
            assertThatThrownBy(() -> categoryService.updateCategory(NOT_FOUND_CATEGORY_ID, any(UpdateCategoryParam.class)))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + CATEGORY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_CATEGORY_ID);

            //verify
            verify(categoryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("카테고리 삭제_예외")
        void deleteCategory_exception() {
            //given
            given(categoryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(CATEGORY, NOT_FOUND_CATEGORY_ID));

            //expected
            assertThatThrownBy(() -> categoryService.deleteCategory(NOT_FOUND_CATEGORY_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + CATEGORY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_CATEGORY_ID);

            //verify
            verify(categoryRepository, times(1)).findById(any(Long.class));
        }
    }
}