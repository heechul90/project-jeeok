package com.jeeok.jeeokshop;

import com.jeeok.jeeokshop.core.delivery.repository.DeliveryQueryRepository;
import com.jeeok.jeeokshop.core.deliveryRider.repository.DeliveryRiderQueryRepository;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class DeliveryTestConfig {

    @PersistenceContext protected EntityManager em;

    @Bean
    public DeliveryQueryRepository deliveryQueryRepository() {
        return new DeliveryQueryRepository(em);
    }

    @Bean public DeliveryRiderQueryRepository deliveryRiderQueryRepository() {
        return new DeliveryRiderQueryRepository(em);
    }

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> configurer.operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint());
    }
}
