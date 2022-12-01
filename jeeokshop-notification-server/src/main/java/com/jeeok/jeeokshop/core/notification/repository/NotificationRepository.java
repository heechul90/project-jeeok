package com.jeeok.jeeokshop.core.notification.repository;

import com.jeeok.jeeokshop.core.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
