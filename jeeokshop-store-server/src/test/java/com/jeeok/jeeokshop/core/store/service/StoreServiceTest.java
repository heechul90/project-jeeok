package com.jeeok.jeeokshop.core.store.service;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.SaveStoreParam;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import com.jeeok.jeeokshop.core.store.dto.UpdateStoreParam;
import com.jeeok.jeeokshop.core.store.repository.StoreQueryRepository;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.jeeok.jeeokshop.common.entity.Yn.Y;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StoreServiceTest extends MockTest {

    //CREATE_STORE
    public static final Long STORE_ID_1 = 1L;
    public static final String CATEGORY_NAME = "category";
    public static final int ORDER = 1;
    public static final String STORE_NAME = "store";
    public static final BusinessHours BUSINESS_HOURS = new BusinessHours("1700", "2200");
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1234", "5678");
    public static final Address ADDRESS = new Address("12345", "서울시");
    public static final Long MEMBER_ID_1 = 1L;

    //UPDATE_STORE
    public static final String UPDATE_STORE_NAME = "update_store";
    public static final BusinessHours UPDATE_BUSINESS_HOURS = new BusinessHours("1800", "2300");
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "8765", "4321");
    public static final Address UPDATE_ADDRESS = new Address("54321", "세종시");

    //ERROR_MESSAGE
    public static final String STORE = "Store";
    public static final String CATEGORY = "Category";
    public static final Long NOT_FOUND_STORE_ID = 0L;
    public static final Long NOT_FOUND_CATEGORY_ID = 0L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    @Mock protected StoreQueryRepository storeQueryRepository;
    @Mock protected StoreRepository storeRepository;
    @Mock protected CategoryRepository categoryRepository;
    @InjectMocks protected StoreService storeService;

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

    Store store = null;

    @BeforeEach
    void beforeEach() {
        List<Category> categories = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> categories.add(getCategory(CATEGORY_NAME + i, ORDER + i)));
        store = getStore(STORE_NAME, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID_1, categories);
    }

    @Nested
    class SuccessfulTest {
        @Test
        @DisplayName("스토어 목록 조회")
        void findStores() {
            //given
            List<Store> stores = new ArrayList<>();
            IntStream.range(0, 5).forEach(i -> stores.add(getStore(STORE_NAME + i, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID_1 + i, new ArrayList<>())));
            given(storeQueryRepository.findStores(any(StoreSearchCondition.class), any(Pageable.class))).willReturn(new PageImpl<>(stores));

            StoreSearchCondition condition = new StoreSearchCondition();
            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Store> content = storeService.findStores(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(5);
            assertThat(content.getContent().size()).isEqualTo(5);

            //verify
            verify(storeQueryRepository, times(1)).findStores(any(StoreSearchCondition.class), any(Pageable.class));
        }

        @Test
        @DisplayName("스토어 단건 조회")
        void findStore() {
            //given
            given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));

            //when
            Store findStore = storeService.findStore(STORE_ID_1);

            //then
            assertThat(findStore.getName()).isEqualTo(STORE_NAME);
            assertThat(findStore.getBusinessHours()).isEqualTo(BUSINESS_HOURS);
            assertThat(findStore.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
            assertThat(findStore.getAddress()).isEqualTo(ADDRESS);
            assertThat(findStore.getCategories()).extracting("name")
                    .containsExactly(CATEGORY_NAME + "0", CATEGORY_NAME + "1", CATEGORY_NAME + "2");
        }

        @Test
        @DisplayName("스토어 저장")
        void saveStore() {
            //given
            given(storeRepository.save(any(Store.class))).willReturn(store);

            List<SaveStoreParam.StoreCategoryParam> storeCategories = new ArrayList<>();
            IntStream.range(0, 3).forEach(i -> storeCategories.add(new SaveStoreParam.StoreCategoryParam(CATEGORY_NAME + i, ORDER + i)));
            SaveStoreParam param = SaveStoreParam.builder()
                    .name(STORE_NAME)
                    .businessHours(BUSINESS_HOURS)
                    .phoneNumber(PHONE_NUMBER)
                    .address(ADDRESS)
                    .storeCategoryParams(storeCategories)
                    .build();

            //when
            Store savedStore = storeService.saveStore(param);

            //then
            assertThat(savedStore.getName()).isEqualTo(STORE_NAME);
            assertThat(savedStore.getBusinessHours()).isEqualTo(BUSINESS_HOURS);
            assertThat(savedStore.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
            assertThat(savedStore.getAddress()).isEqualTo(ADDRESS);
            assertThat(savedStore.getCategories().size()).isEqualTo(3);

            //verify
            verify(storeRepository, times(1)).save(any(Store.class));
        }

        @Test
        @DisplayName("스토어 수정")
        void updateStore() {
            //given
            Category category = getCategory("new" + CATEGORY_NAME, ORDER + 5);
            store.getCategories().add(category);

            given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));

            List<UpdateStoreParam.StoreCategoryParam> storeCategoryParams = new ArrayList<>();
            storeCategoryParams.add(
                    UpdateStoreParam.StoreCategoryParam.builder()
                            .name("new" + CATEGORY_NAME)
                            .order(ORDER + 5)
                            .build());

            UpdateStoreParam param = UpdateStoreParam.builder()
                    .name(UPDATE_STORE_NAME)
                    .businessHours(UPDATE_BUSINESS_HOURS)
                    .phoneNumber(UPDATE_PHONE_NUMBER)
                    .address(UPDATE_ADDRESS)
                    .storeCategoryParams(storeCategoryParams)
                    .build();

            //when
            storeService.updateStore(STORE_ID_1, param);

            //then
            assertThat(store.getName()).isEqualTo(UPDATE_STORE_NAME);
            assertThat(store.getBusinessHours()).isEqualTo(UPDATE_BUSINESS_HOURS);
            assertThat(store.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);
            assertThat(store.getAddress()).isEqualTo(UPDATE_ADDRESS);
            assertThat(store.getCategories().size()).isEqualTo(4);

            //verify
            verify(storeRepository, times(1)).findById(any(Long.class));
            verify(categoryRepository, times(0)).findById(any(Long.class));
            verify(categoryRepository, times(1)).save(any(Category.class));
        }

        @Test
        @DisplayName("스토어 삭제")
        void deleteStore() {
            //given
            given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));

            //when
            storeService.deleteStore(STORE_ID_1);

            //then
            assertThat(store.getDeleteYn()).isEqualTo(Y);

            //verify
            verify(storeRepository, times(1)).findById(any(Long.class));
        }
    }

    @Nested
    class EntityNotFoundTest {
        @Test
        @DisplayName("스토어 단건 조회_예외")
        void findStore_exception() {
            //given
            given(storeRepository.findById(any(Long.class))).willThrow(new EntityNotFound(STORE, NOT_FOUND_STORE_ID));

            //when
            assertThatThrownBy(() -> storeService.findStore(NOT_FOUND_STORE_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + STORE)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_STORE_ID);

            //then
            verify(storeRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("스토어 수정_예외")
        void updateStore_exception() {
            //given
            given(storeRepository.findById(any(Long.class))).willThrow(new EntityNotFound(STORE, NOT_FOUND_STORE_ID));

            //expected
            assertThatThrownBy(() -> storeService.updateStore(NOT_FOUND_STORE_ID, any(UpdateStoreParam.class)))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + STORE)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_STORE_ID);

            //then
            verify(storeRepository, times(1)).findById(any(Long.class));
        }
    }
}