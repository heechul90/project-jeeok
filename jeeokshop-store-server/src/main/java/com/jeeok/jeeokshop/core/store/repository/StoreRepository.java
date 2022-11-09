package com.jeeok.jeeokshop.core.store.repository;

import com.jeeok.jeeokshop.core.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
