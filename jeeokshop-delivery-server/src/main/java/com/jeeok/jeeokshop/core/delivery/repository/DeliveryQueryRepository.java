package com.jeeok.jeeokshop.core.delivery.repository;

import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.delivery.dto.DeliverySearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeeok.jeeokshop.core.delivery.domain.QDelivery.delivery;

@Repository
public class DeliveryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public DeliveryQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 배송 목록 조회
     */
    public Page<Delivery> findDeliveries(DeliverySearchCondition condition, Pageable pageable) {

        List<Delivery> content = getDeliveryList(condition, pageable);

        JPAQuery<Long> count = getDeliveryListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);

    }

    /**
     * 배송 목록
     */
    private List<Delivery> getDeliveryList(DeliverySearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(delivery)
                .from(delivery)
                .where(
                        searchDeliveryStatusEq(condition.getSearchDeliveryStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 배송 목록 카운트
     */
    private JPAQuery<Long> getDeliveryListCount(DeliverySearchCondition condition) {
        return queryFactory
                .select(delivery.count())
                .from(delivery)
                .where(
                        searchDeliveryStatusEq(condition.getSearchDeliveryStatus())
                );
    }

    /**
     * where status == searchDeliveryStatus
     */
    private BooleanExpression searchDeliveryStatusEq(DeliveryStatus searchDeliveryStatus) {
        return searchDeliveryStatus != null ? delivery.status.eq(searchDeliveryStatus) : null;
    }
}
