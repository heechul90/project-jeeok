package com.jeeok.jeeokshop.core.notification.service;

import com.jeeok.jeeokshop.client.store.StoreClient;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.notification.domain.Notification;
import com.jeeok.jeeokshop.core.notification.repository.NotificationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class NotificationServiceTest extends MockTest {

    //CREATE_NOTIFICATION
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long MEMBER_ID_2 = 2L;
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final Long NOTIFICATION_ID_1 = 1L;

    @InjectMocks protected NotificationService notificationService;
    @Mock protected NotificationRepository notificationRepository;
    @Mock protected StoreClient storeClient;

    private Notification getNotification(Long memberId, String title, String message) {
        return Notification.createNotification()
                .memberId(memberId)
                .title(title)
                .message(message)
                .build();
    }

    Notification notification;

    @BeforeEach
    void beforeEach() {
        notification = getNotification(MEMBER_ID_1, TITLE, MESSAGE);
    }

    @Test
    @DisplayName("알림 단건 조회")
    void findNotifications() {
        //given
        given(notificationRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(notification));

        //when
        Notification findNotification = notificationService.findNotification(NOTIFICATION_ID_1);

        //then
        assertThat(findNotification.getMemberId()).isEqualTo(MEMBER_ID_1);
    }

    @Test
    void findNotification() {
        //given

        //when

        //then
    }

    @Test
    void saveNotification() {
        //given

        //when

        //then
    }

    @Test
    void deleteNotification() {
        //given

        //when

        //then
    }
}