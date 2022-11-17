package com.jeeok.jeeokshop.core.delivery.repository;

import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
