package com.jeeok.jeeokshop.core.notification.service;

import com.jeeok.jeeokshop.client.store.FindItemResponse;
import com.jeeok.jeeokshop.client.store.StoreClient;
import com.jeeok.jeeokshop.core.notification.domain.Notification;
import com.jeeok.jeeokshop.core.notification.dto.NotificationSearchCondition;
import com.jeeok.jeeokshop.core.notification.dto.SaveNotificationParam;
import com.jeeok.jeeokshop.core.notification.exception.NotificationNotFound;
import com.jeeok.jeeokshop.core.notification.repository.NotificationQueryRepository;
import com.jeeok.jeeokshop.core.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationQueryRepository notificationQueryRepository;
    private final NotificationRepository notificationRepository;

    private final StoreClient storeClient;

    /**
     * 알람 목록 조회
     */
    public Page<Notification> findNotifications(NotificationSearchCondition condition, Pageable pageable) {
        return notificationQueryRepository.findNotifications(condition, pageable);
    }

    /**
     * 알람 단건 조회
     */
    public Notification findNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFound(notificationId));
    }

    /**
     * 알람 저장
     */
    @Transactional
    public Notification saveNotification(SaveNotificationParam param) {
        FindItemResponse findItem = storeClient.findItem(param.getItemId()).getData();

        String storeName = "[" + findItem.getStoreName() + "] ";
        String itemNam = "[" + findItem.getItemName() + "] ";

        Notification notification = Notification.createNotification()
                .memberId(param.getMemberId())
                .title(storeName + param.getTitle())
                .message(storeName + itemNam + param.getMessage())
                .build();
        return notificationRepository.save(notification);

    }

    /**
     * 알람 삭제
     */
    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification findNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFound(notificationId));
        notificationRepository.delete(findNotification);
    }
}
