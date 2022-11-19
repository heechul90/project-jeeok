package com.jeeok.jeeokshop.core.item.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.Photo;
import com.jeeok.jeeokshop.common.entity.Yn;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.item.controller.request.SaveItemRequest;
import com.jeeok.jeeokshop.core.item.controller.request.UpdateItemRequest;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.jeeok.jeeokshop.core.item.service.ItemService;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminItemControllerTest extends IntegrationTest {

    //CREATE_ITEM
    public static final String ITEM_NAME = "교촌오리지날";
    public static final int PRICE = 10000;
    public static final int STOCK_QUANTITY = 100;
    public static final Photo PHOTO = new Photo("https://jeeok.com/item/picture", "교촌오리지날");
    public static final Yn SALES_YN = Yn.Y;
    public static final Long MEMBER_ID_1 = 1L;

    //REQUEST_INFO
    public static final String API_FIND_ITEMS = "/admin/items";
    public static final String API_FIND_ITEM = "/admin/items/{itemId}";
    public static final String API_SAVE_ITEM = "/admin/items";
    public static final String API_UPDATE_ITEM = "/admin/items/{itemId}";
    public static final String API_DELETE_ITEM = "/admin/items/{itemId}";

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @PersistenceContext protected EntityManager em;
    @Autowired protected ItemService itemService;

    Category category;
    Store store;

    @BeforeEach
    void beforeEach() {
        category = Category.createCategory()
                .name("시리즈 카테고리")
                .order(1)
                .build();

        store = Store.createStore()
                .name("교촌치킨")
                .businessHours(new BusinessHours("1700", "2200"))
                .phoneNumber(new PhoneNumber("010", "1111", "2222"))
                .address(new Address("11234", "서울시"))
                .memberId(MEMBER_ID_1)
                .categories(List.of(category))
                .build();

        em.persist(store);
    }

    private Item getItem(String name, int price, int stockQuantity, Photo photo) {
        Item item = Item.createItem()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .photo(photo)
                .store(store)
                .category(category)
                .build();
        return item;
    }

    @Test
    @DisplayName("상품 목록 조회")
    void findItems() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getItem(ITEM_NAME + i, PRICE, STOCK_QUANTITY, PHOTO)));

        ItemSearchCondition condition = new ItemSearchCondition();
        condition.setSearchCondition(SearchCondition.NAME);
        condition.setSearchKeyword(ITEM_NAME);

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_ITEMS)
                .queryParams(conditionParams)
                .queryParams(pageRequestParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(10)))
                .andDo(print())
                .andDo(document("admin-findItems",
                        requestParameters(
                                parameterWithName("searchStoreId").ignored(),
                                parameterWithName("searchCondition").description("검색 조건"),
                                parameterWithName("searchKeyword").description("검색 키워드"),
                                parameterWithName("page").description("검색 페이지"),
                                parameterWithName("size").description("검색 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data[*].itemId").description("상품 고유번호"),
                                fieldWithPath("data[*].itemName").description("상품 이름"),
                                fieldWithPath("data[*].salesYn").description("판매여부"),
                                fieldWithPath("data[*].price").description("상품 가격"),
                                fieldWithPath("data[*].stockQuantity").description("재고수량"),
                                fieldWithPath("data[*].photo").description("상품 이미지")
                        )
                ));

    }

    @Test
    @DisplayName("상품 단건 조회")
    void findItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_ITEM, item.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.itemId").value(item.getId()))
                .andExpect(jsonPath("$.data.itemName").value(ITEM_NAME))
                .andExpect(jsonPath("$.data.salesYn").value(SALES_YN.name()))
                .andExpect(jsonPath("$.data.price").value(PRICE))
                .andExpect(jsonPath("$.data.photo").value(PHOTO.fullPhotoPath()))
                .andDo(print())
                .andDo(document("admin-findItem",
                        pathParameters(
                                parameterWithName("itemId").description("상품 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.itemId").description("상품 고유번호"),
                                fieldWithPath("data.itemName").description("상품 이름"),
                                fieldWithPath("data.salesYn").description("판매여부"),
                                fieldWithPath("data.price").description("상품 가격"),
                                fieldWithPath("data.stockQuantity").description("재고수량"),
                                fieldWithPath("data.photo").description("상품 이미지")
                        )
                ));
    }

    @Test
    @DisplayName("상품 저장")
    void saveItem() throws Exception {
        //given
        SaveItemRequest request = SaveItemRequest.builder()
                .itemName(ITEM_NAME)
                .itemPrice(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .photoPath(PHOTO.getPath())
                .photoName(PHOTO.getName())
                .storeId(store.getId())
                .categoryId(category.getId())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_SAVE_ITEM)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-saveItem",
                        requestFields(
                                fieldWithPath("itemName").description("상품 이름"),
                                fieldWithPath("itemPrice").description("상품 가격"),
                                fieldWithPath("stockQuantity").description("재고수량"),
                                fieldWithPath("photoPath").description("이미지 주소"),
                                fieldWithPath("photoName").description("이미지 이름"),
                                fieldWithPath("storeId").description("스토어 고유번호"),
                                fieldWithPath("categoryId").description("카테고리 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.savedItemId").description("저장된 상품 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("상품 수정")
    void updateItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        UpdateItemRequest request = UpdateItemRequest.builder()
                .itemName(ITEM_NAME)
                .salesYn(SALES_YN)
                .itemPrice(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .photoPath(PHOTO.getPath())
                .photoName(PHOTO.getName())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_UPDATE_ITEM, item.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedItemId").value(item.getId()))
                .andDo(print())
                .andDo(document("admin-updateItem",
                        pathParameters(
                                parameterWithName("itemId").description("상품 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("itemName").description("상품 이름"),
                                fieldWithPath("salesYn").description("판매 여부"),
                                fieldWithPath("itemPrice").description("상품 가격"),
                                fieldWithPath("stockQuantity").description("재고수량"),
                                fieldWithPath("photoPath").description("이미지 주소"),
                                fieldWithPath("photoName").description("이미지 이름")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.updatedItemId").description("수정된 상품 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_ITEM, item.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-deleteItem",
                        pathParameters(
                                parameterWithName("itemId").description("상품 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data").description("데이터")
                        )
                ));
    }
}