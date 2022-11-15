package com.jeeok.jeeokshop.core.favoritestore.repository;

import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore, Long> {
}
