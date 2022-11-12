package com.jeeok.jeeokshop;

import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
import com.jeeok.jeeokshop.core.store.repository.StoreQueryRepository;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import com.jeeok.jeeokshop.core.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class StoreTestConfig {

    @PersistenceContext EntityManager em;

    @Autowired StoreRepository storeRepository;

    @Autowired CategoryRepository categoryRepository;

    @Bean
    public StoreService storeService() {
        return new StoreService(storeQueryRepository(), storeRepository, categoryRepository);
    }

    @Bean
    public StoreQueryRepository storeQueryRepository() {
        return new StoreQueryRepository(em);
    }
}
