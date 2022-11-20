package com.jeeok.jeeokshop.core.store.repository;

import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class StoreRepositoryTest extends RepositoryTest {

    //CREATE_STORE
    public static final String CATEGORY_NAME = "category";
    public static final int ORDER = 1;
    public static final String STORE_NAME = "store";
    public static final BusinessHours BUSINESS_HOURS = new BusinessHours("1700", "2200");
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1234", "5678");
    public static final Address ADDRESS = new Address("12345", "서울시");
    public static final Long MEMBER_ID = 0L;

    //UPDATE_STORE
    public static final String UPDATE_STORE_NAME = "update_store";
    public static final BusinessHours UPDATE_BUSINESS_HOURS = new BusinessHours("1800", "2300");
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "8765", "4321");
    public static final Address UPDATE_ADDRESS = new Address("54321", "세종시");
    public static final long MEMBER_ID_1 = 1L;
    public static final long MEMBER_ID_2 = 2L;

    @PersistenceContext protected EntityManager em;
    @Autowired protected StoreQueryRepository storeQueryRepository;
    @Autowired protected StoreRepository storeRepository;

    private Store getStore(String name, BusinessHours businessHours, PhoneNumber phoneNumber, Address address, Long memberId, List<Category> categories) {
        return Store.createStore()
                .name(name)
                .businessHours(businessHours)
                .phoneNumber(phoneNumber)
                .address(address)
                .memberId(memberId)
                .categories(categories)
                .build();
    }

    @Nested
    class SuccessfulTest {
        @Test
        @DisplayName("스토어 목록 조회")
        void findStores() {
            //given
            IntStream.range(0, 20).forEach(i -> em.persist(getStore(STORE_NAME + i, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, i % 2 == 0 ? MEMBER_ID_1 : MEMBER_ID_2, new ArrayList<>())));

            StoreSearchCondition condition = new StoreSearchCondition();
            condition.setSearchCondition(SearchCondition.NAME);
            condition.setSearchKeyword(STORE_NAME);
            condition.setSearchMemberId(MEMBER_ID_1);

            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Store> content = storeQueryRepository.findStores(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(10);
            assertThat(content.getContent().size()).isEqualTo(10);
        }
    }
}