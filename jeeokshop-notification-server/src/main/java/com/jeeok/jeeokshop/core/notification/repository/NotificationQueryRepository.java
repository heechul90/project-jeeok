package com.jeeok.jeeokshop.core.notification.repository;

import com.jeeok.jeeokshop.core.notification.domain.Notification;
import com.jeeok.jeeokshop.core.notification.dto.NotificationSearchCondition;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class NotificationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public NotificationQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<Notification> findNotifications(NotificationSearchCondition condition, Pageable pageable) {

        return null;
    }
}
