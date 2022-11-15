package com.jeeok.jeeokshop.core.item.repository;

import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.Photo;
import com.jeeok.jeeokshop.common.entity.Yn;
import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest extends RepositoryTest {

    //CREATE_ITEM
    public static final String ITEM_NAME = "item_name";
    public static final int PRICE = 10000;
    public static final Photo PHOTO = new Photo("photo_path", "photo_name");
    public static final Yn SALES_YN = Yn.Y;

    @PersistenceContext EntityManager em;

    @Autowired protected ItemRepository itemRepository;

    @Autowired protected ItemQueryRepository itemQueryRepository;

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

    private Item getItem(String name, int price, Photo photo) {
        return Item.createItem()
                .name(name)
                .price(price)
                .photo(photo)
                .store(store)
                .category(category)
                .build();
    }

    @Nested
    class SuccessfulTest {

        @Test
        @DisplayName("상품 목록 조회")
        @Rollback(value = false)
        void findItems() {
            //given
            IntStream.range(0, 15).forEach(i -> em.persist(getItem(ITEM_NAME + i, PRICE, PHOTO)));

            ItemSearchCondition condition = new ItemSearchCondition();
            condition.setSearchCondition(SearchCondition.NAME);
            condition.setSearchKeyword(ITEM_NAME);

            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Item> content = itemQueryRepository.findItems(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(15);
            assertThat(content.getContent().size()).isEqualTo(10);
        }
    }

    @Nested
    class UnsuccessfulTest {

    }
}