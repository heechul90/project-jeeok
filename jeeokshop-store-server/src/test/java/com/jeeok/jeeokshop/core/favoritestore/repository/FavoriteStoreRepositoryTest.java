package com.jeeok.jeeokshop.core.favoritestore.repository;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import com.jeeok.jeeokshop.core.favoritestore.dto.FavoriteStoreSearchCondition;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteStoreRepositoryTest extends RepositoryTest {

    //CREATE_FAVORITE_STORE
    public static final long MEMBER_10 = 10L;
    public static final long MEMBER_20 = 20L;

    @PersistenceContext protected EntityManager em;
    @Autowired protected FavoriteStoreQueryRepository favoriteStoreQueryRepository;
    @Autowired protected FavoriteStoreRepository favoriteStoreRepository;

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

        em.persist(store);
    }

    private FavoriteStore getFavoriteStore(long memberId) {
        return FavoriteStore.createFavoriteStore()
                .memberId(memberId)
                .store(store)
                .build();
    }

    @Nested
    class SuccessfulTest {

        @Test
        @DisplayName("호감 스토어 목록 조회")
        void findFavoriteStores() {
            //given
            getFavoriteStore(10L);
            IntStream.range(0, 20).forEach(i -> em.persist(getFavoriteStore(i % 2 == 0 ? MEMBER_10 : MEMBER_20)));

            FavoriteStoreSearchCondition condition = new FavoriteStoreSearchCondition();
            condition.setSearchMemberId(MEMBER_10);

            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<FavoriteStore> content = favoriteStoreQueryRepository.findFavoriteStores(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(10);
            assertThat(content.getContent().size()).isEqualTo(10);
        }
    }
}