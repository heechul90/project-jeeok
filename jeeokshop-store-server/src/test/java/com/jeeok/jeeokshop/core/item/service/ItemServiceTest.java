package com.jeeok.jeeokshop.core.item.service;

import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.Photo;
import com.jeeok.jeeokshop.common.entity.Yn;
import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.jeeok.jeeokshop.core.item.dto.SaveItemParam;
import com.jeeok.jeeokshop.core.item.dto.UpdateItemParam;
import com.jeeok.jeeokshop.core.item.repository.ItemQueryRepository;
import com.jeeok.jeeokshop.core.item.repository.ItemRepository;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ItemServiceTest extends MockTest {

    //CREATE_ITEM
    public static final String ITEM_NAME = "item_name";
    public static final int PRICE = 10000;
    public static final int STOCK_QUANTITY = 100;
    public static final Photo PHOTO = new Photo("photo_path", "photo_name");
    public static final Yn SALES_YN = Yn.Y;
    public static final long ITEM_ID = 1L;
    public static final long STORE_ID = 1L;
    public static final long CATEGORY_ID = 1L;

    //UPDATE_ITEM
    public static final String UPDATE_ITEM_NAME = "update_item_name";
    public static final Yn UPDATE_SALES_YN = Yn.N;
    public static final int UPDATE_PRICE = 20000;
    public static final int UPDATE_STOCK_QUANTITY = 50;
    public static final Photo UPDATE_PHOTO = new Photo("/image", "photo_name");

    //ERROR_MESSAGE
    public static final String ITEM = "Member";
    public static final String STORE = "Store";
    public static final String CATEGORY = "Category";
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    @Mock private ItemQueryRepository itemQueryRepository;

    @Mock private ItemRepository itemRepository;

    @Mock private StoreRepository storeRepository;

    @Mock private CategoryRepository categoryRepository;

    @InjectMocks private ItemService itemService;

    Category category;
    Store store;

    @BeforeEach
    void beforeEach() {
        category = Category.createCategory()
                .name("category_name")
                .order(1)
                .build();

        store = Store.createStore()
                .name("store_name")
                .businessHours(new BusinessHours("1700", "2200"))
                .phoneNumber(new PhoneNumber("010", "1111", "2222"))
                .address(new Address("11234", "서울시"))
                .memberId(1L)
                .categories(List.of(category))
                .build();
    }

    private Item getItem(String name, int price, int stockQuantity, Photo photo) {
        return Item.createItem()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .photo(photo)
                .store(store)
                .category(category)
                .build();
    }

    @Nested
    class successfulTest {

        @Test
        @DisplayName("상품 목록 조회")
        void findItems() {
            //given
            List<Item> items = new ArrayList<>();
            IntStream.range(0, 5).forEach(i -> items.add(getItem(ITEM_NAME + i, PRICE, STOCK_QUANTITY, PHOTO)));
            given(itemQueryRepository.findItems(any(ItemSearchCondition.class), any(Pageable.class))).willReturn(new PageImpl<>(items));

            ItemSearchCondition condition = new ItemSearchCondition();
            condition.setSearchCondition(SearchCondition.NAME);
            condition.setSearchKeyword(ITEM_NAME);

            PageRequest pagerequest = PageRequest.of(0, 10);

            //when
            Page<Item> content = itemService.findItems(condition, pagerequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(5);
            assertThat(content.getContent().size()).isEqualTo(5);

            //verify
            verify(itemQueryRepository, times(1)).findItems(any(ItemSearchCondition.class), any(Pageable.class));
        }

        @Test
        @DisplayName("상품 단건 조회")
        void findItem() {
            //given
            Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
            given(itemRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(item));

            //when
            Item findItem = itemService.findItem(ITEM_ID);

            //then
            assertThat(findItem.getName()).isEqualTo(ITEM_NAME);
            assertThat(findItem.getSalesYn()).isEqualTo(SALES_YN);
            assertThat(findItem.getPrice()).isEqualTo(PRICE);
            assertThat(findItem.getStockQuantity()).isEqualTo(STOCK_QUANTITY);
            assertThat(findItem.getPhoto()).isEqualTo(PHOTO);
            assertThat(findItem.getCategory()).isEqualTo(category);
            assertThat(findItem.getStore()).isEqualTo(store);

            //verify
            verify(itemRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("상품 저장")
        void saveItem() {
            //given
            Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
            given(itemRepository.save(any(Item.class))).willReturn(item);
            given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));
            given(categoryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(category));

            SaveItemParam param = SaveItemParam.builder()
                    .name(ITEM_NAME)
                    .price(PRICE)
                    .photo(PHOTO)
                    .build();

            //when
            Item savedItem = itemService.saveItem(param, STORE_ID, CATEGORY_ID);

            //then
            assertThat(savedItem.getName()).isEqualTo(ITEM_NAME);
            assertThat(savedItem.getSalesYn()).isEqualTo(SALES_YN);
            assertThat(savedItem.getPrice()).isEqualTo(PRICE);
            assertThat(savedItem.getStockQuantity()).isEqualTo(STOCK_QUANTITY);
            assertThat(savedItem.getPhoto()).isEqualTo(PHOTO);
            assertThat(savedItem.getStore()).isEqualTo(store);
            assertThat(savedItem.getCategory()).isEqualTo(category);

            //verify
            verify(itemRepository, times(1)).save(any(Item.class));
            verify(storeRepository, times(1)).findById(any(Long.class));
            verify(categoryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("상품 수정")
        void updateItem() {
            //given
            Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
            given(itemRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(item));

            UpdateItemParam param = UpdateItemParam.builder()
                    .name(UPDATE_ITEM_NAME)
                    .yn(UPDATE_SALES_YN)
                    .price(UPDATE_PRICE)
                    .stockQuantity(UPDATE_STOCK_QUANTITY)
                    .photo(UPDATE_PHOTO)
                    .build();

            //when
            itemService.updateItem(ITEM_ID, param);

            //then
            assertThat(item.getName()).isEqualTo(UPDATE_ITEM_NAME);
            assertThat(item.getSalesYn()).isEqualTo(UPDATE_SALES_YN);
            assertThat(item.getPrice()).isEqualTo(UPDATE_PRICE);
            assertThat(item.getStockQuantity()).isEqualTo(UPDATE_STOCK_QUANTITY);
            assertThat(item.getPhoto()).isEqualTo(UPDATE_PHOTO);

            //verify
            verify(itemRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("상품 삭제")
        void deleteItem() {
            //given
            Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
            given(itemRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(item));

            //when
            itemService.deleteItem(ITEM_ID);

            //then

            //verify
            verify(itemRepository, times(1)).findById(any(Long.class));
            verify(itemRepository, times(1)).delete(any(Item.class));
        }
    }

    @Nested
    class UnsuccessfulTest {

        @Test
        @DisplayName("멤버 단건 조회_entityNotFound_예외")
        void findItem_entityNotFound_exception() {
            //given
            given(itemRepository.findById(any(Long.class))).willThrow(new EntityNotFound(ITEM, NOT_FOUND_ID.toString()));

            //expected
            assertThatThrownBy(() -> itemService.findItem(NOT_FOUND_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + ITEM)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);
        }

        @Test
        @DisplayName("멤버 저장_entityNotFound_예외")
        void saveItem_store_entityNotFound_exception() {
            //given
            given(storeRepository.findById(any(Long.class))).willThrow(new EntityNotFound(STORE, NOT_FOUND_ID.toString()));

            //expected
            assertThatThrownBy(() -> itemService.saveItem(any(SaveItemParam.class), NOT_FOUND_ID, NOT_FOUND_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + STORE)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);
        }

        @Test
        @DisplayName("멤버 저장_entityNotFound_예외")
        void saveItem_category_entityNotFound_exception() {
            //given
            given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));
            given(categoryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(CATEGORY, NOT_FOUND_ID.toString()));

            //expected
            assertThatThrownBy(() -> itemService.saveItem(any(SaveItemParam.class), NOT_FOUND_ID, NOT_FOUND_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + CATEGORY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);
        }

        @Test
        @DisplayName("멤버 수정_entityNotFound_예외")
        void updateItem_entityNotFound_exception() {
            //given
            given(itemRepository.findById(any(Long.class))).willThrow(new EntityNotFound(ITEM, NOT_FOUND_ID.toString()));

            //expected
            assertThatThrownBy(() -> itemService.updateItem(NOT_FOUND_ID, any(UpdateItemParam.class)))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + ITEM)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);
        }

        @Test
        @DisplayName("멤버 삭제_entityNotFound_예외")
        void deleteItem_entityNotFound_exception() {
            //given
            given(itemRepository.findById(any(Long.class))).willThrow(new EntityNotFound(ITEM, NOT_FOUND_ID.toString()));

            //expected
            assertThatThrownBy(() -> itemService.deleteItem(NOT_FOUND_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + ITEM)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);
        }
    }
}