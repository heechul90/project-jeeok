package com.jeeok.jeeokshop.core.favoritestore.repository;

import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import com.jeeok.jeeokshop.core.favoritestore.dto.FavoriteStoreSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeeok.jeeokshop.core.favoritestore.domain.QFavoriteStore.*;
import static com.jeeok.jeeokshop.core.store.domain.QStore.*;

@Repository
public class FavoriteStoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteStoreQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 호감 스토어 목록 조회
     */
    public Page<FavoriteStore> findFavoriteStores(FavoriteStoreSearchCondition condition, Pageable pageable) {

        List<FavoriteStore> content = getFavoriteStoreList(condition, pageable);

        JPAQuery<Long> count = getFavoriteStoreListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 호감 스토어 목록
     */
    private List<FavoriteStore> getFavoriteStoreList(FavoriteStoreSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(favoriteStore)
                .from(favoriteStore)
                .join(favoriteStore.store, store)
                .where(
                        searchMemberIdEq(condition.getSearchMemberId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 호감 스토어 목록 카운트
     */
    private JPAQuery<Long> getFavoriteStoreListCount(FavoriteStoreSearchCondition condition) {
        return queryFactory
                .select(favoriteStore.count())
                .from(favoriteStore)
                .where(
                        searchMemberIdEq(condition.getSearchMemberId())
                );
    }

    /**
     * where memberId == searchMemberId
     */
    private BooleanExpression searchMemberIdEq(Long searchMemberId) {
        return searchMemberId != null ? favoriteStore.memberId.eq(searchMemberId) : null;
    }
}
