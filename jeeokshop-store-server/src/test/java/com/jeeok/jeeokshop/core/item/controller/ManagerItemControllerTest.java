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
import com.jeeok.jeeokshop.core.item.controller.request.EditItemRequest;
import com.jeeok.jeeokshop.core.item.controller.request.RegisterItemRequest;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
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

class ManagerItemControllerTest extends IntegrationTest {

    //CREATE_ITEM
    public static final String ITEM_NAME = "??????????????????";
    public static final int PRICE = 10000;
    public static final int STOCK_QUANTITY = 100;
    public static final Photo PHOTO = new Photo("https://jeeok.com/item/picture", "??????????????????");
    public static final Yn SALES_YN = Yn.Y;
    public static final Long MEMBER_ID_1 = 1L;

    //REQUEST_INFO
    public static final String API_FIND_ITEMS = "/manager/stores/{storeId}/items";
    public static final String API_FIND_ITEM = "/manager/stores/{storeId}/items/{itemId}";
    public static final String API_REGISTER_ITEM = "/manager/stores/{storeId}/items";
    public static final String API_EDIT_ITEM = "/manager/stores/{storeId}/items/{itemId}";
    public static final String API_DELETE_ITEM = "/manager/stores/{storeId}/items/{itemId}";

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @PersistenceContext protected EntityManager em;
    @Autowired protected ItemService itemService;

    Category category;
    Store store;

    @BeforeEach
    void beforeEach() {
        category = Category.createCategory()
                .name("????????? ????????????")
                .order(1)
                .build();

        store = Store.createStore()
                .name("????????????")
                .businessHours(new BusinessHours("1700", "2200"))
                .phoneNumber(new PhoneNumber("010", "1111", "2222"))
                .address(new Address("11234", "?????????"))
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
    @DisplayName("??? ????????? ?????? ??????")
    void findItems() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getItem(ITEM_NAME + i, PRICE, STOCK_QUANTITY, PHOTO)));

        ItemSearchCondition condition = new ItemSearchCondition();
        condition.setSearchCondition(SearchCondition.NAME);
        condition.setSearchKeyword(ITEM_NAME);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_ITEMS, store.getId())
                .queryParams(conditionParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(15)))
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("manager-findItems",
                        pathParameters(
                                parameterWithName("storeId").description("????????? ????????????")
                        ),
                        requestParameters(
                                parameterWithName("searchStoreId").ignored(),
                                parameterWithName("searchCondition").description("?????? ??????"),
                                parameterWithName("searchKeyword").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data[*].itemId").description("?????? ????????????"),
                                fieldWithPath("data[*].itemName").description("?????? ??????"),
                                fieldWithPath("data[*].salesYn").description("????????????"),
                                fieldWithPath("data[*].price").description("?????? ??????"),
                                fieldWithPath("data[*].stockQuantity").description("????????????"),
                                fieldWithPath("data[*].photo").description("?????? ?????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ????????? ?????? ??????")
    void findItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_ITEM, store.getId(), item.getId()));

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
                .andDo(document("manager-findItem",
                        pathParameters(
                                parameterWithName("storeId").description("????????? ????????????"),
                                parameterWithName("itemId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.itemId").description("?????? ????????????"),
                                fieldWithPath("data.itemName").description("?????? ??????"),
                                fieldWithPath("data.salesYn").description("????????????"),
                                fieldWithPath("data.price").description("?????? ??????"),
                                fieldWithPath("data.stockQuantity").description("????????????"),
                                fieldWithPath("data.photo").description("?????? ?????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void registerItem() throws Exception {
        //given
        RegisterItemRequest request = RegisterItemRequest.builder()
                .itemName(ITEM_NAME)
                .itemPrice(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .photoPath(PHOTO.getPath())
                .photoName(PHOTO.getName())
                .categoryId(category.getId())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_REGISTER_ITEM, store.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("manager-registerItem",
                        pathParameters(
                                parameterWithName("storeId").description("????????? ????????????")
                        ),
                        requestFields(
                                fieldWithPath("itemName").description("?????? ??????"),
                                fieldWithPath("itemPrice").description("?????? ??????"),
                                fieldWithPath("stockQuantity").description("????????????"),
                                fieldWithPath("photoPath").description("????????? ??????"),
                                fieldWithPath("photoName").description("????????? ??????"),
                                fieldWithPath("categoryId").description("???????????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.savedItemId").description("????????? ?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ????????? ??????")
    void editItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        EditItemRequest request = EditItemRequest.builder()
                .itemName(ITEM_NAME)
                .salesYn(SALES_YN)
                .itemPrice(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .photoPath(PHOTO.getPath())
                .photoName(PHOTO.getName())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_EDIT_ITEM, store.getId(), item.getId())
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
                .andDo(document("manager-editItem",
                        pathParameters(
                                parameterWithName("storeId").description("????????? ????????????"),
                                parameterWithName("itemId").description("?????? ????????????")
                        ),
                        requestFields(
                                fieldWithPath("itemName").description("?????? ??????"),
                                fieldWithPath("salesYn").description("?????? ??????"),
                                fieldWithPath("itemPrice").description("?????? ??????"),
                                fieldWithPath("stockQuantity").description("????????????"),
                                fieldWithPath("photoPath").description("????????? ??????"),
                                fieldWithPath("photoName").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.updatedItemId").description("????????? ?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ????????? ?????? ??????")
    void deleteItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_ITEM, store.getId(), item.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("manager-deleteItem",
                        pathParameters(
                                parameterWithName("storeId").description("????????? ????????????"),
                                parameterWithName("itemId").description("?????? ????????????")
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