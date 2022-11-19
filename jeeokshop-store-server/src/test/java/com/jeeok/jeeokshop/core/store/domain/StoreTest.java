package com.jeeok.jeeokshop.core.store.domain;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class StoreTest extends RepositoryTest {

    //STORE
    public static final String STORE_NAME = "store";
    public static final BusinessHours BUSINESS_HOURS = new BusinessHours("1700", "2200");
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1111", "222");
    public static final Address ADDRESS = new Address("12345", "Sejong");

    //CATEGORY
    public static final String CATEGORY_NAME = "category";
    public static final int ORDER = 1;

    @PersistenceContext protected EntityManager em;
    @Autowired protected StoreRepository storeRepository;

    @Test
    @DisplayName("스토어 생성 테스트")
    void createStore() {
        //given
        List<Category> categories = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> categories.add(Category.createCategory().name(CATEGORY_NAME + i).order(ORDER + i).build()));

        Store store = Store.createStore()
                .name(STORE_NAME)
                .businessHours(BUSINESS_HOURS)
                .phoneNumber(PHONE_NUMBER)
                .address(ADDRESS)
                .categories(categories)
                .build();

        //when
        em.persist(store);

        //then
        Store findStore = em.find(Store.class, store.getId());
        assertThat(findStore.getName()).isEqualTo(STORE_NAME);
        assertThat(findStore.getBusinessHours().getOpeningHours()).isEqualTo(BUSINESS_HOURS.getOpeningHours());
        assertThat(findStore.getBusinessHours().getClosingHours()).isEqualTo(BUSINESS_HOURS.getClosingHours());
        assertThat(findStore.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        assertThat(findStore.getAddress()).isEqualTo(ADDRESS);
        assertThat(findStore.getCategories()).extracting("name")
                .containsExactly(CATEGORY_NAME + "0", CATEGORY_NAME + "1", CATEGORY_NAME + "2");
        assertThat(findStore.getCategories()).extracting("order")
                .containsExactly(1, 2, 3);
        assertThat(findStore.getCategories().get(0).getStore().getName()).isEqualTo(STORE_NAME);
    }
}