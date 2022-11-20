package com.jeeok.jeeokshop.core.category.repository;

import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.dto.CategorySearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryTest extends RepositoryTest {

    //CREATE_CATEGORY
    public static final String NAME = "category_name";
    public static final int ORDER = 1;

    @PersistenceContext protected EntityManager em;

    @Autowired protected CategoryQueryRepository categoryQueryRepository;

    @Autowired protected CategoryRepository categoryRepository;

    private Category getCategory(String name, int order) {
        return Category.createCategory()
                .name(name)
                .order(order)
                .build();
    }

    @Nested
    class SuccessfulTest {
        @Test
        @DisplayName("카테고리 목록 조회")
        void findCategories() {
            //given
            IntStream.range(0, 15).forEach(i -> em.persist(getCategory(NAME, i)));

            CategorySearchCondition condition = new CategorySearchCondition();
            condition.setSearchCondition(SearchCondition.NAME);
            condition.setSearchKeyword(NAME);

            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Category> content = categoryQueryRepository.findCategories(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(15);
            assertThat(content.getContent().size()).isEqualTo(10);
        }
    }
}