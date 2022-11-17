package com.jeeok.jeeokshop.core.delivery.repository;

import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByMemberIdAndOrderId(Long memberId, Long orderId);
}
