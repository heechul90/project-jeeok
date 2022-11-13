package com.jeeok.jeeokshop.core.item.repository;

import com.jeeok.jeeokshop.core.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
