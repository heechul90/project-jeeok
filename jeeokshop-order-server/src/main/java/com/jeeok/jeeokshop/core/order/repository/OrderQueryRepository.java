package com.jeeok.jeeokshop.core.order.repository;

import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.dto.OrderSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeeok.jeeokshop.core.order.domain.QOrder.order;

@Repository
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public OrderQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 주문 목록 조회
     */
    public Page<Order> findOrders(OrderSearchCondition condition, Pageable pageable) {

        List<Order> content = getOrderList(condition, pageable);

        JPAQuery<Long> count = getOrderListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 주문 목록
     */
    private List<Order> getOrderList(OrderSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(order)
                .from(order)
                .where(
                        searchMemberIdEq(condition.getSearchMemberId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 주문 목록 카운트
     */
    private JPAQuery<Long> getOrderListCount(OrderSearchCondition condition) {
        return queryFactory
                .select(order.count())
                .from(order)
                .where(
                        searchMemberIdEq(condition.getSearchMemberId())
                );
    }

    /**
     * where memberId == searchMemberId
     */
    private BooleanExpression searchMemberIdEq(Long searchMemberId) {
        return searchMemberId != null ? order.memberId.eq(searchMemberId) : null;
    }

}
