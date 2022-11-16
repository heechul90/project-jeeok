package com.jeeok.jeeokshop.core.order.repository;

import com.jeeok.jeeokshop.core.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
