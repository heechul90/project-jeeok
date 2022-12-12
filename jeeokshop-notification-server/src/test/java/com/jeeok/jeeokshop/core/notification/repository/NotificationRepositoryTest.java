package com.jeeok.jeeokshop.core.notification.repository;

import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.notification.domain.Notification;
import com.jeeok.jeeokshop.core.notification.dto.NotificationSearchCondition;
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

class NotificationRepositoryTest extends RepositoryTest {

    //CREATE_NOTIFICATION
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long MEMBER_ID_2 = 2L;
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";

    @PersistenceContext protected EntityManager em;
    @Autowired protected NotificationQueryRepository notificationQueryRepository;

    private Notification getNotification(Long memberId, String title, String message) {
        return Notification.createNotification()
                .memberId(memberId)
                .title(title)
                .message(message)
                .build();
    }

    @Nested
    class SuccessfulTest {

        @Test
        @DisplayName("알림 목록 조회")
        void findNotifications() {
            //given
            IntStream.range(0, 16).forEach(i -> em.persist(getNotification((i % 2 == 0 ? MEMBER_ID_1 : MEMBER_ID_2), TITLE, MESSAGE)));

            NotificationSearchCondition condition = new NotificationSearchCondition();
            condition.setSearchMemberId(MEMBER_ID_1);

            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Notification> content = notificationQueryRepository.findNotifications(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(8);
            assertThat(content.getContent().size()).isEqualTo(8);
        }
    }
}