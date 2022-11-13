package com.jeeok.jeeokshop.core.item.repository;

import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.jeeok.jeeokshop.core.item.domain.QItem.*;
import static org.springframework.util.StringUtils.*;

@Repository
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ItemQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 상품 목록 조회
     */
    public Page<Item> findItems(ItemSearchCondition condition, Pageable pageable) {
        List<Item> content = getItemList(condition, pageable);

        JPAQuery<Long> count = getItemListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);

    }

    /**
     * 상품 목록
     */
    private List<Item> getItemList(ItemSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(item)
                .from(item)
                .where(
                        searchConditionContains(condition.getSearchCondition(), condition.getSearchKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 상품 목록 카운트
     */
    private JPAQuery<Long> getItemListCount(ItemSearchCondition condition) {
        return queryFactory
                .select(item.count())
                .from(item)
                .where(
                        searchConditionContains(condition.getSearchCondition(), condition.getSearchKeyword())
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
            return item.name.contains(searchKeyword);
        }

        return null;
    }
}
