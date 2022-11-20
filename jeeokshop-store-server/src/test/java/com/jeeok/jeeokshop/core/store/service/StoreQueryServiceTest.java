package com.jeeok.jeeokshop.core.store.service;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.SaveStoreParam;
import com.jeeok.jeeokshop.core.store.dto.UpdateStoreParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class StoreQueryServiceTest extends RepositoryTest {

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

    @PersistenceContext protected EntityManager em;
    @Autowired protected StoreService storeService;

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

    private Category getCategory(String category, Integer order) {
        return Category.createCategory()
                .name(category)
                .order(order)
                .build();
    }

    @Nested
    @DisplayName("스토어 객체지향 테스트")
    class objectCheckTest {
        @Test
        @DisplayName("스토어 저장")
        void saveStore() {
            //given
            List<SaveStoreParam.StoreCategoryParam> storeCategoryParams = new ArrayList<>();
            IntStream.range(0, 3).forEach(i -> storeCategoryParams.add(
                    SaveStoreParam.StoreCategoryParam.builder()
                            .name(CATEGORY_NAME + i)
                            .order(ORDER + i)
                            .build()
            ));
            SaveStoreParam param = SaveStoreParam.builder()
                    .name(STORE_NAME)
                    .businessHours(BUSINESS_HOURS)
                    .phoneNumber(PHONE_NUMBER)
                    .address(ADDRESS)
                    .memberId(MEMBER_ID)
                    .storeCategoryParams(storeCategoryParams)
                    .build();

            //when
            Store savedStore = storeService.saveStore(param);

            //then
            assertThat(savedStore.getName()).isEqualTo(STORE_NAME);
            assertThat(savedStore.getBusinessHours()).isEqualTo(BUSINESS_HOURS);
            assertThat(savedStore.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
            assertThat(savedStore.getAddress()).isEqualTo(ADDRESS);
            assertThat(savedStore.getCategories().size()).isEqualTo(3);
            assertThat(savedStore.getCategories()).extracting("name")
                    .containsExactly(CATEGORY_NAME + "0", CATEGORY_NAME + "1", CATEGORY_NAME + "2");
            assertThat(savedStore.getCategories()).extracting("order")
                    .containsExactly(1, 2, 3);

            assertThat(savedStore.getCategories().get(0).getStore()).isEqualTo(savedStore);
            assertThat(savedStore.getCategories().get(1).getStore()).isEqualTo(savedStore);
            assertThat(savedStore.getCategories().get(2).getStore()).isEqualTo(savedStore);
        }

        @Test
        @DisplayName("스토어 수정")
        void updateStore() {
            //given
            List<Category> categories = new ArrayList<>();
            IntStream.range(0, 3).forEach(i -> categories.add(getCategory(CATEGORY_NAME + i, ORDER + i)));
            Store store = getStore(STORE_NAME, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID, categories);
            em.persist(store);
            em.flush();
            em.clear();

            List<UpdateStoreParam.StoreCategoryParam> storeCategoryParams = store.getCategories().stream()
                    .map(category -> UpdateStoreParam.StoreCategoryParam.builder()
                            .categoryId(category.getId())
                            .name(category.getName())
                            .order(category.getOrder())
                            .build()
                    ).collect(Collectors.toList());
            storeCategoryParams.add(
                    UpdateStoreParam.StoreCategoryParam.builder()
                            .name(CATEGORY_NAME + "new")
                            .order(4)
                            .build()
            );

            UpdateStoreParam param = UpdateStoreParam.builder()
                    .name(UPDATE_STORE_NAME)
                    .businessHours(UPDATE_BUSINESS_HOURS)
                    .phoneNumber(UPDATE_PHONE_NUMBER)
                    .address(UPDATE_ADDRESS)
                    .storeCategoryParams(storeCategoryParams)
                    .build();

            //when
            storeService.updateStore(store.getId(), param);

            //then
            Store findStore = em.find(Store.class, store.getId());
            assertThat(findStore.getName()).isEqualTo(UPDATE_STORE_NAME);
            assertThat(findStore.getBusinessHours()).isEqualTo(UPDATE_BUSINESS_HOURS);
            assertThat(findStore.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);
            assertThat(findStore.getAddress()).isEqualTo(UPDATE_ADDRESS);
            assertThat(findStore.getCategories().size()).isEqualTo(4);
            assertThat(findStore.getCategories()).extracting("name")
                    .containsExactly(CATEGORY_NAME + "0", CATEGORY_NAME + "1", CATEGORY_NAME + "2", CATEGORY_NAME + "new");
            assertThat(findStore.getCategories()).extracting("order")
                    .containsExactly(1, 2, 3, 4);

            assertThat(findStore.getCategories().get(0).getStore()).isEqualTo(findStore);
            assertThat(findStore.getCategories().get(1).getStore()).isEqualTo(findStore);
            assertThat(findStore.getCategories().get(2).getStore()).isEqualTo(findStore);
            assertThat(findStore.getCategories().get(3).getStore()).isEqualTo(findStore);
        }
    }
}