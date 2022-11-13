package com.jeeok.jeeokshop.core.store.repository;

import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.jeeok.jeeokshop.core.store.domain.QStore.*;
import static org.springframework.util.StringUtils.*;

@Repository
public class StoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    public StoreQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 스토어 목록 조회
     */
    public Page<Store> findStores(StoreSearchCondition condition, Pageable pageable) {

        List<Store> content = getStoreList(condition, pageable);

        JPAQuery<Long> count = getStoreListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 스토어 목록
     */
    private List<Store> getStoreList(StoreSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(store)
                .from(store)
                .where(
                        searchConditionContains(condition.getSearchCondition(), condition.getSearchKeyword()),
                        searchMemberIdEq(condition.getSearchMemberId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(store.id.desc())
                .fetch();
    }

    /**
     * 스토어 목록 카운트
     */
    private JPAQuery<Long> getStoreListCount(StoreSearchCondition condition) {
        return queryFactory
                .select(store.count())
                .from(store)
                .where(
                        searchConditionContains(condition.getSearchCondition(), condition.getSearchKeyword()),
                        searchMemberIdEq(condition.getSearchMemberId())
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
            return store.name.contains(searchKeyword);
        }

        return null;
    }

    /**
     * where memberId == searchMemberId
     */
    private BooleanExpression searchMemberIdEq(Long searchMemberId) {
        return searchMemberId == null ? null : store.memberId.eq(searchMemberId);
    }
}
