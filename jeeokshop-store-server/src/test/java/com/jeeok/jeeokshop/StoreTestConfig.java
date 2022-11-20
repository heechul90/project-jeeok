package com.jeeok.jeeokshop;

import com.jeeok.jeeokshop.core.category.repository.CategoryQueryRepository;
import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
import com.jeeok.jeeokshop.core.favoritestore.repository.FavoriteStoreQueryRepository;
import com.jeeok.jeeokshop.core.item.repository.ItemQueryRepository;
import com.jeeok.jeeokshop.core.store.repository.StoreQueryRepository;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import com.jeeok.jeeokshop.core.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class StoreTestConfig {

    @PersistenceContext protected EntityManager em;
    @Autowired protected StoreRepository storeRepository;
    @Autowired protected CategoryRepository categoryRepository;

    @Bean
    public StoreService storeService() {
        return new StoreService(storeQueryRepository(), storeRepository, categoryRepository);
    }

    @Bean
    public StoreQueryRepository storeQueryRepository() {
        return new StoreQueryRepository(em);
    }

    @Bean
    public ItemQueryRepository itemQueryRepository() {
        return new ItemQueryRepository(em);
    }

    @Bean
    public FavoriteStoreQueryRepository favoriteStoreQueryRepository() {
        return new FavoriteStoreQueryRepository(em);
    }

    @Bean
    public CategoryQueryRepository categoryQueryRepository() {
        return new CategoryQueryRepository(em);
    }

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer.operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint());
    }
}
