package com.jeeok.jeeokshop.core.deliveryRider.repository;

import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import com.jeeok.jeeokshop.core.deliveryRider.dto.DeliveryRiderSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeeok.jeeokshop.core.deliveryRider.domain.QDeliveryRider.deliveryRider;

@Repository
public class DeliveryRiderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public DeliveryRiderQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 배송 라이더 목록 조회
     */
    public Page<DeliveryRider> findDeliveryRiders(DeliveryRiderSearchCondition condition, Pageable pageable) {

        List<DeliveryRider> content = getDeliveryRiderList(condition, pageable);

        JPAQuery<Long> count = getDeliveryRiderListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 배송 라이더 목록
     */
    private List<DeliveryRider> getDeliveryRiderList(DeliveryRiderSearchCondition condition, Pageable pageable) {
        List<DeliveryRider> content = queryFactory
                .select(deliveryRider)
                .from(deliveryRider)
                .where(
                        searchRiderId(condition.getSearchRiderId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    /**
     * 배송 라이더 목록 카운트
     */
    private JPAQuery<Long> getDeliveryRiderListCount(DeliveryRiderSearchCondition condition) {
        JPAQuery<Long> count = queryFactory
                .select(deliveryRider.count())
                .from(deliveryRider)
                .where(
                        searchRiderId(condition.getSearchRiderId())
                );
        return count;
    }

    /**
     * where riderId == searchRiderId
     */
    private BooleanExpression searchRiderId(Long searchRiderId) {
        return searchRiderId != null ? deliveryRider.riderId.eq(searchRiderId) : null;
    }
}
