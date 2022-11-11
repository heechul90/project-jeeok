package com.jeeok.jeeokshop.core.category.repository;

import com.jeeok.jeeokshop.core.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
