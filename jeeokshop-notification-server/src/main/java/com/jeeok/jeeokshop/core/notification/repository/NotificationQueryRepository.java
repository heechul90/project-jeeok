package com.jeeok.jeeokshop.core.notification.repository;

import com.jeeok.jeeokshop.core.notification.domain.Notification;
import com.jeeok.jeeokshop.core.notification.dto.NotificationSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeeok.jeeokshop.core.notification.domain.QNotification.notification;

@Repository
public class NotificationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public NotificationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 알람 목록 조회
     */
    public Page<Notification> findNotifications(NotificationSearchCondition condition, Pageable pageable) {

        List<Notification> content = getNotificationList(condition, pageable);

        JPAQuery<Long> count = getNotificationListCount(condition);

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * 알람 목록
     */
    private List<Notification> getNotificationList(NotificationSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(notification)
                .from(notification)
                .where(
                        searchMemberIdEq(condition.getSearchMemberId())
                )
                .orderBy(notification.memberId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 알람 목록 카운트
     */
    private JPAQuery<Long> getNotificationListCount(NotificationSearchCondition condition) {
        return queryFactory
                .select(notification.count())
                .from(notification)
                .where(
                        searchMemberIdEq(condition.getSearchMemberId())
                );
    }

    /**
     * where memberId == searchMemberId
     */
    private BooleanExpression searchMemberIdEq(Long searchMemberId) {
        return searchMemberId != null ? notification.memberId.eq(searchMemberId) : null;
    }
}
