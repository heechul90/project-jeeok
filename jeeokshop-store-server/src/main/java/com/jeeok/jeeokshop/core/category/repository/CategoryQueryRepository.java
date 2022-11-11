package com.jeeok.jeeokshop.core.category.repository;

import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.dto.CategorySearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeeok.jeeokshop.core.category.domain.QCategory.category;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CategoryQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 카테고리 목록 조회
     */
    public Page<Category> findCategories(CategorySearchCondition condition, Pageable pageable) {

        List<Category> content = getCategoryList(condition, pageable);

        JPAQuery<Long> count = getCategoryListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 카테고리 목록
     */
    private List<Category> getCategoryList(CategorySearchCondition condition, Pageable pageable) {
         return queryFactory
                .select(category)
                .from(category)
                .where(
                        searchConditionContains(condition.getSearchCondition(), condition.getSearchKeyword()),
                        searchMemberIdIn(condition.getSearchMemberId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 카테고리 목록 카운트
     */
    private JPAQuery<Long> getCategoryListCount(CategorySearchCondition condition) {
         return queryFactory
                .select(category.count())
                .from(category)
                .where(
                        searchConditionContains(condition.getSearchCondition(), condition.getSearchKeyword()),
                        searchMemberIdIn(condition.getSearchMemberId())
                );
    }

    /**
     * where searchCondition like '%searchKeyword%'
     */
    private BooleanExpression searchConditionContains(SearchCondition searchCondition, String searchKeyword) {
        if (searchCondition == null || !hasText(searchKeyword)) {
            return null;
        }

        if (searchCondition.equals(SearchCondition.NAME)) {
            return category.name.contains(searchKeyword);
        }

        return null;
    }

    private BooleanExpression searchMemberIdIn(Long searchMemberId) {
        return null;
    }
}
