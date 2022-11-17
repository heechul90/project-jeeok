package com.jeeok.jeeokshop;

import com.jeeok.jeeokshop.core.order.repository.OrderQueryRepository;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class OrderTestConfig {

    @PersistenceContext protected EntityManager em;

    //order repository
    @Bean
    public OrderQueryRepository orderQueryRepository() {
        return new OrderQueryRepository(em);
    }

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer.operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint());
    }
}
