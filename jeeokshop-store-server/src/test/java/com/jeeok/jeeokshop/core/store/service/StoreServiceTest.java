package com.jeeok.jeeokshop.core.store.service;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.SaveStoreParam;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import com.jeeok.jeeokshop.core.store.dto.UpdateStoreParam;
import com.jeeok.jeeokshop.core.store.repository.StoreQueryRepository;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

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

    @InjectMocks StoreService storeService;

    @Mock StoreQueryRepository storeQueryRepository;

    @Mock StoreRepository storeRepository;

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
        store = getStore(STORE_NAME, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID, categories);
    }

    @Test
    void findStores() {
        //given
        List<Store> stores = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> stores.add(getStore(STORE_NAME + i, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID + i, new ArrayList<>())));
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
    void findStore() {
        //given
        given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));

        //when
        Store findStore = storeService.findStore(0L);

        //then
        assertThat(findStore.getName()).isEqualTo(STORE_NAME);
        assertThat(findStore.getBusinessHours()).isEqualTo(BUSINESS_HOURS);
        assertThat(findStore.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        assertThat(findStore.getAddress()).isEqualTo(ADDRESS);
        assertThat(findStore.getCategories()).extracting("name")
                .containsExactly(CATEGORY_NAME + "0", CATEGORY_NAME + "1", CATEGORY_NAME + "2");
    }

    @Test
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
                .storeCategories(storeCategories)
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
    void updateStore() {
        //given
        given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));

        UpdateStoreParam param = UpdateStoreParam.builder()
                .name(UPDATE_STORE_NAME)
                .businessHours(UPDATE_BUSINESS_HOURS)
                .phoneNumber(UPDATE_PHONE_NUMBER)
                .address(UPDATE_ADDRESS)
                .build();

        //when
        storeService.updateStore(0L, param);

        //then
        assertThat(store.getName()).isEqualTo(UPDATE_STORE_NAME);
        assertThat(store.getBusinessHours()).isEqualTo(UPDATE_BUSINESS_HOURS);
        assertThat(store.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);
        assertThat(store.getAddress()).isEqualTo(UPDATE_ADDRESS);

        //verify
        verify(storeRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void deleteStore() {
        //given
        given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));

        //when
        storeService.deleteStore(0L);

        //then

        //verify
        verify(storeRepository, times(1)).findById(any(Long.class));
        verify(storeRepository, times(1)).delete(any(Store.class));
    }
}