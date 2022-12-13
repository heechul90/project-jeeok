package com.jeeok.jeeokshop.core.notification.service;

import com.jeeok.jeeokshop.client.store.FindItemResponse;
import com.jeeok.jeeokshop.client.store.StoreClient;
import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.notification.domain.Notification;
import com.jeeok.jeeokshop.core.notification.dto.SaveNotificationParam;
import com.jeeok.jeeokshop.core.notification.exception.NotificationNotFound;
import com.jeeok.jeeokshop.core.notification.repository.NotificationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NotificationServiceTest extends MockTest {

    //CREATE_NOTIFICATION
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long MEMBER_ID_2 = 2L;
    public static final Long NOTIFICATION_ID_1 = 1L;

    public static final Long ITEM_ID_1 = 1L;
    public static final String ITEM_NAME = "item_name";
    public static final int PRICE = 10000;
    public static final Long STORE_ID_1 = 1L;
    public static final String STORE_NAME = "store_name";
    public static final String PHONE_NUMBER = "01033334444";
    public static final Long CATEGORY_ID_1 = 1L;
    public static final String CATEGORY_NAME = "category_name";

    public static final String TITLE = "[" + STORE_NAME + "] title" ;
    public static final String MESSAGE = "[" + STORE_NAME + "] [" + ITEM_NAME + "] message";

    //ERROR MESSAGE
    public static final Long NOT_FOUND_NOTIFICATION_ID_0 = 0L;

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
        String title = TITLE;
        String message = MESSAGE;
        notification = getNotification(MEMBER_ID_1, title, message);
    }

    @Nested
    class SuccessfulTest {

        @Test
        @DisplayName("알림 단건 조회")
        void findNotification() {
            //given
            given(notificationRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(notification));

            //when
            Notification findNotification = notificationService.findNotification(NOTIFICATION_ID_1);

            //then
            assertThat(findNotification.getMemberId()).isEqualTo(MEMBER_ID_1);
            assertThat(findNotification.getTitle()).isEqualTo(TITLE);
            assertThat(findNotification.getMessage()).isEqualTo(MESSAGE);

            //verify
            verify(notificationRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("알림 저장")
        void saveNotification() {
            //given
            FindItemResponse findItem = FindItemResponse.builder()
                    .itemId(ITEM_ID_1)
                    .itemName(ITEM_NAME)
                    .price(PRICE)
                    .storeId(STORE_ID_1)
                    .storeName(STORE_NAME)
                    .phoneNumber(PHONE_NUMBER)
                    .categoryId(CATEGORY_ID_1)
                    .categoryName(CATEGORY_NAME)
                    .build();

            given(storeClient.findItem(any(Long.class))).willReturn(JsonResult.OK(findItem));
            given(notificationRepository.save(any(Notification.class))).willReturn(notification);

            SaveNotificationParam param = SaveNotificationParam.builder()
                    .memberId(MEMBER_ID_1)
                    .itemId(ITEM_ID_1)
                    .title(TITLE)
                    .message(MESSAGE)
                    .build();

            //when
            Notification saveNotification = notificationService.saveNotification(param);

            //then
            assertThat(saveNotification.getMemberId()).isEqualTo(MEMBER_ID_1);
            assertThat(saveNotification.getTitle()).isEqualTo(TITLE);
            assertThat(saveNotification.getMessage()).isEqualTo(MESSAGE);

            //verify
            verify(storeClient, times(1)).findItem(any(Long.class));
            verify(notificationRepository, times(1)).save(any(Notification.class));
        }

        @Test
        @DisplayName("알림 삭제")
        void deleteNotification() {
            //given
            given(notificationRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(notification));

            //when
            notificationService.deleteNotification(NOTIFICATION_ID_1);

            //then

            //verify
            verify(notificationRepository, times(1)).findById(any(Long.class));
            verify(notificationRepository, times(1)).delete(any(Notification.class));
        }

    }

    @Nested
    class NotificationNotFoundTest {

        @Test
        @DisplayName("알림 단건 조회_예외")
        void findNotification_exception() {
            //given
            given(notificationRepository.findById(any(Long.class))).willThrow(new NotificationNotFound(NOT_FOUND_NOTIFICATION_ID_0));

            //expected
            assertThatThrownBy(() -> notificationService.findNotification(NOT_FOUND_NOTIFICATION_ID_0))
                    .isInstanceOf(NotificationNotFound.class)
                    .hasMessage("Notification Entity Not Found. id=" + NOT_FOUND_NOTIFICATION_ID_0);
        }
    }
}