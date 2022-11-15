package com.jeeok.jeeokshop.core.favoritestore.service;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import com.jeeok.jeeokshop.core.favoritestore.dto.FavoriteStoreSearchCondition;
import com.jeeok.jeeokshop.core.favoritestore.repository.FavoriteStoreQueryRepository;
import com.jeeok.jeeokshop.core.favoritestore.repository.FavoriteStoreRepository;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class FavoriteStoreServiceTest extends MockTest {

    //CREATE_FAVORITE_STORE
    public static final Long FAVORITE_STORE_ID = 1L;
    public static final Long MEMBER_10 = 10L;
    public static final Long STORE_ID = 1L;

    //ERROR_MESSAGE
    public static final String FAVORITE_STORE = "FavoriteStore";
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";
    public static final String STORE = "Store";
    @Mock protected FavoriteStoreQueryRepository favoriteStoreQueryRepository;

    @Mock protected FavoriteStoreRepository favoriteStoreRepository;

    @Mock protected StoreRepository storeRepository;

    @InjectMocks protected FavoriteStoreService favoriteStoreService;

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
            List<FavoriteStore> favoriteStores = new ArrayList<>();
            IntStream.range(0, 5).forEach(i -> favoriteStores.add(getFavoriteStore(MEMBER_10)));
            given(favoriteStoreService.findFavoriteStores(any(FavoriteStoreSearchCondition.class), any(Pageable.class))).willReturn(new PageImpl<>(favoriteStores));

            FavoriteStoreSearchCondition condition = new FavoriteStoreSearchCondition();
            condition.setSearchMemberId(MEMBER_10);

            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<FavoriteStore> content = favoriteStoreService.findFavoriteStores(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(5);
            assertThat(content.getContent().size()).isEqualTo(5);

            //verify
            verify(favoriteStoreQueryRepository, times(1)).findFavoriteStores(any(FavoriteStoreSearchCondition.class), any(Pageable.class));
        }

        @Test
        @DisplayName("호감 스토어 단건 조회")
        void findFavoriteStore() {
            //given
            FavoriteStore favoriteStore = getFavoriteStore(MEMBER_10);
            given(favoriteStoreRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(favoriteStore));

            //when
            favoriteStoreService.findFavoriteStore(FAVORITE_STORE_ID);

            //then
            assertThat(favoriteStore.getMemberId()).isEqualTo(MEMBER_10);
            assertThat(favoriteStore.getStore()).isEqualTo(store);

            //verify
            verify(favoriteStoreRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("호감 스토어 저장")
        void addFavoriteStore() {
            //given
            FavoriteStore favoriteStore = getFavoriteStore(MEMBER_10);
            given(storeRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(store));
            given(favoriteStoreRepository.save(any(FavoriteStore.class))).willReturn(favoriteStore);

            //when
            FavoriteStore savedFavoriteStore = favoriteStoreService.saveFavoriteStore(MEMBER_10, STORE_ID);

            //then
            assertThat(savedFavoriteStore.getMemberId()).isEqualTo(MEMBER_10);
            assertThat(savedFavoriteStore.getStore()).isEqualTo(store);

            //verify
            verify(favoriteStoreRepository, times(1)).save(any(FavoriteStore.class));
            verify(storeRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("호감 스토어 삭제")
        void deleteFavoriteStore() {
            //given
            FavoriteStore favoriteStore = getFavoriteStore(MEMBER_10);
            given(favoriteStoreRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(favoriteStore));

            //when
            favoriteStoreService.deleteFavoriteStore(FAVORITE_STORE_ID);

            //then

            //verify
            verify(favoriteStoreRepository, times(1)).findById(any(Long.class));
            verify(favoriteStoreRepository, times(1)).delete(any(FavoriteStore.class));
        }
    }

    @Nested
    class UnsuccessfulTest {

        @Test
        @DisplayName("호감 스토어 단건 조회_예외")
        void findFavoriteStore_entityNotFound() {
            //given
            given(favoriteStoreRepository.findById(any(Long.class))).willThrow(new EntityNotFound(FAVORITE_STORE, FAVORITE_STORE_ID.toString()));

            //expected
            assertThatThrownBy(() -> favoriteStoreService.findFavoriteStore(FAVORITE_STORE_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + FAVORITE_STORE)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + FAVORITE_STORE_ID);
        }

        @Test
        @DisplayName("호감 스토어 단건 조회_예외")
        void saveFavoriteStore_entityNotFound() {
            //given
            given(storeRepository.findById(any(Long.class))).willThrow(new EntityNotFound(STORE, STORE_ID.toString()));

            //expected
            assertThatThrownBy(() -> favoriteStoreService.saveFavoriteStore(MEMBER_10, STORE_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + STORE)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + STORE_ID);
        }

        @Test
        @DisplayName("호감 스토어 단건 조회_예외")
        void deleteFavoriteStore_entityNotFound() {
            //given
            given(favoriteStoreRepository.findById(any(Long.class))).willThrow(new EntityNotFound(FAVORITE_STORE, FAVORITE_STORE_ID.toString()));

            //expected
            assertThatThrownBy(() -> favoriteStoreService.deleteFavoriteStore(FAVORITE_STORE_ID))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + FAVORITE_STORE)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + FAVORITE_STORE_ID);
        }
    }
}