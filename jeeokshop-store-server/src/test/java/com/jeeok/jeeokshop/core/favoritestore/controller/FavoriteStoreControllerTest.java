package com.jeeok.jeeokshop.core.favoritestore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import com.jeeok.jeeokshop.core.favoritestore.dto.FavoriteStoreSearchCondition;
import com.jeeok.jeeokshop.core.favoritestore.service.FavoriteStoreService;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteStoreControllerTest extends IntegrationTest {

    //CREATE_FAVORITE_STORE
    public static final Long FAVORITE_STORE_ID = 1L;
    public static final Long MEMBER_ID_10 = 10L;
    public static final Long MEMBER_ID_20 = 20L;
    public static final Long STORE_ID = 1L;

    //ERROR_MESSAGE
    public static final String FAVORITE_STORE = "FavoriteStore";
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE_STARTING_WITH = "???????????? ?????? ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";
    public static final String STORE = "Store";

    //REQUEST_INFO
    public static final String HEADER_NAME = "member-id";
    public static final String API_FIND_FAVORITE_STORES = "/front/favoriteStores";
    public static final String API_FIND_FAVORITE_STORE = "/front/favoriteStores/{favoriteStoreId}";
    public static final String API_ADD_FAVORITE_STORE = "/front/favoriteStores/stores/{storeId}";
    public static final String API_DELETE_FAVORITE_STORE = "/front/favoriteStores/{favoriteStoreId}";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @PersistenceContext
    protected EntityManager em;
    @Autowired
    protected FavoriteStoreService favoriteStoreService;

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
                .address(new Address("11234", "?????????"))
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

    @Test
    @DisplayName("??? ?????? ????????? ??????")
    void findMyFavoriteStores() throws Exception {
        //given
        IntStream.range(0, 16).forEach(i -> em.persist(getFavoriteStore(i % 2 == 0 ? MEMBER_ID_10 : MEMBER_ID_20)));

        FavoriteStoreSearchCondition condition = new FavoriteStoreSearchCondition();

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {
        }));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_FAVORITE_STORES)
                .header(HEADER_NAME, MEMBER_ID_10)
                .queryParams(conditionParams)
                .queryParams(pageRequestParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(8)))
                .andDo(print())
                .andDo(document("findMyFavoriteStores",
                        requestParameters(
                                parameterWithName("searchMemberId").ignored(),
                                parameterWithName("page").description("?????? ?????????"),
                                parameterWithName("size").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data[*].favoriteStoreId").description("?????? ????????? ????????????"),
                                fieldWithPath("data[*].storeId").description("?????? ????????? ????????????"),
                                fieldWithPath("data[*].storeName").description("?????? ????????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ?????? ?????????")
    void findMyFavoriteStore() throws Exception {
        //given
        FavoriteStore favoriteStore = getFavoriteStore(MEMBER_ID_10);
        em.persist(favoriteStore);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_FAVORITE_STORE, favoriteStore.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.favoriteStoreId").value(favoriteStore.getId()))
                .andExpect(jsonPath("$.data.storeId").value(store.getId()))
                .andExpect(jsonPath("$.data.storeName").value(store.getName()))
                .andDo(print())
                .andDo(document("findMyFavoriteStore",
                        pathParameters(
                                parameterWithName("favoriteStoreId").description("?????? ????????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.favoriteStoreId").description("?????? ????????? ????????????"),
                                fieldWithPath("data.storeId").description("?????? ????????? ????????????"),
                                fieldWithPath("data.storeName").description("?????? ????????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ?????? ????????? ??????")
    void addMyFavoriteStore() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(post(API_ADD_FAVORITE_STORE, store.getId())
                .header(HEADER_NAME, MEMBER_ID_10));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("addMyFavoriteStore",
                        pathParameters(
                                parameterWithName("storeId").description("????????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.addedFavoriteStoreId").description("????????? ?????? ????????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ?????? ????????? ??????")
    void deleteFavoriteStore() throws Exception {
        //given
        FavoriteStore favoriteStore = getFavoriteStore(MEMBER_ID_10);
        em.persist(favoriteStore);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_FAVORITE_STORE, favoriteStore.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("deleteMyFavoriteStore",
                        pathParameters(
                                parameterWithName("favoriteStoreId").description("?????? ????????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").description("?????????")
                        )
                ));
    }
}