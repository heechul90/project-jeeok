package com.jeeok.jeeokshop.core.delivery.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.entity.PhoneNumber;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.delivery.controller.request.SaveDeliveryRequest;
import com.jeeok.jeeokshop.core.delivery.controller.request.UpdateDeliveryRequest;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.delivery.dto.DeliverySearchCondition;
import com.jeeok.jeeokshop.core.delivery.service.DeliveryService;
import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDeliveryControllerTest extends IntegrationTest {

    //CREATE_DELIVERY
    public static final Long DELIVERY_ID_1 = 1L;
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final Address ADDRESS = new Address("38273", "?????????");

    //UPDATE_DELIVERY
    public static final Address UPDATE_ADDRESS = new Address("33871", "?????????");

    //CREATE_DELIVERY_RIDER
    public static final long RIDER_ID_11 = 11L;
    public static final String RIDER_NAME = "?????????";
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "4823", "1009");

    //REQUEST_INFO
    public static final String API_FIND_DELIVERIES = "/admin/deliveries";
    public static final String API_FIND_DELIVERY = "/admin/deliveries/{deliveryId}";
    public static final String API_SAVE_DELIVERY = "/admin/deliveries";
    public static final String API_UPDATE_DELIVERY = "/admin/deliveries/{deliveryId}";
    public static final String API_DELIVERY = "/admin/deliveries/{deliveryId}/delivery";
    public static final String API_COMPLETE = "/admin/deliveries/{deliveryId}/complete";
    public static final String API_CANCEL = "/admin/deliveries/{deliveryId}/cancel";
    public static final String API_DELETE_DELIVERY = "/admin/deliveries/{deliveryId}";

    @PersistenceContext protected EntityManager em;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected DeliveryService deliveryService;

    private Delivery getDelivery(Address address, Long memberId, Long orderId) {
        return Delivery.createDelivery()
                .address(address)
                .memberId(memberId)
                .orderId(orderId)
                .build();
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void findDeliveries() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1)));

        DeliverySearchCondition condition = new DeliverySearchCondition();
        condition.setSearchDeliveryStatus(DeliveryStatus.READY);

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_DELIVERIES)
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
                .andDo(document("admin-findDeliveries",
                        requestParameters(
                                parameterWithName("searchDeliveryStatus").description("?????? ??????"),
                                parameterWithName("page").description("?????? ?????????"),
                                parameterWithName("size").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data[*].deliveryId").description("?????? ????????????"),
                                fieldWithPath("data[*].address").description("?????? ??????"),
                                fieldWithPath("data[*].deliveryStatus").description("?????? ??????"),
                                fieldWithPath("data[*].memberId").description("?????? ????????????"),
                                fieldWithPath("data[*].orderId").description("?????? ????????????"),
                                fieldWithPath("data[*].riderId").description("????????? ????????????"),
                                fieldWithPath("data[*].riderName").description("????????? ??????"),
                                fieldWithPath("data[*].phoneNumber").description("???????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void findDelivery() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        DeliveryRider.createDeliveryRider()
                .riderId(RIDER_ID_11)
                .riderName(RIDER_NAME)
                .phoneNumber(PHONE_NUMBER)
                .delivery(delivery)
                .build();
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_DELIVERY, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.deliveryId").value(delivery.getId()))
                .andExpect(jsonPath("$.data.address").value(ADDRESS.fullAddress()))
                .andExpect(jsonPath("$.data.deliveryStatus").value(DeliveryStatus.READY.getMessage()))
                .andExpect(jsonPath("$.data.memberId").value(MEMBER_ID_1))
                .andExpect(jsonPath("$.data.orderId").value(ORDER_ID_1))
                .andExpect(jsonPath("$.data.riderId").value(RIDER_ID_11))
                .andExpect(jsonPath("$.data.riderName").value(RIDER_NAME))
                .andExpect(jsonPath("$.data.phoneNumber").value(PHONE_NUMBER.fullPhoneNumber()))
                .andDo(print())
                .andDo(document("admin-findDelivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.deliveryId").description("?????? ????????????"),
                                fieldWithPath("data.address").description("?????? ??????"),
                                fieldWithPath("data.deliveryStatus").description("?????? ??????"),
                                fieldWithPath("data.memberId").description("?????? ????????????"),
                                fieldWithPath("data.orderId").description("?????? ????????????"),
                                fieldWithPath("data.riderId").description("????????? ????????????"),
                                fieldWithPath("data.riderName").description("????????? ??????"),
                                fieldWithPath("data.phoneNumber").description("???????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void saveDelivery() throws Exception {
        //given
        SaveDeliveryRequest request = SaveDeliveryRequest.builder()
                .memberId(MEMBER_ID_1)
                .orderId(ORDER_ID_1)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_SAVE_DELIVERY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-saveDelivery",
                        requestFields(
                                fieldWithPath("memberId").description("?????? ????????????"),
                                fieldWithPath("orderId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.savedDeliveryId").description("????????? ?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void updateDelivery() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        UpdateDeliveryRequest request = UpdateDeliveryRequest.builder()
                .zipcode(UPDATE_ADDRESS.getZipcode())
                .address(UPDATE_ADDRESS.getAddress())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_UPDATE_DELIVERY, delivery.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedDeliveryId").value(delivery.getId()))
                .andDo(print())
                .andDo(document("admin-updateDelivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("?????? ????????????")
                        ),
                        requestFields(
                                fieldWithPath("zipcode").description("????????????"),
                                fieldWithPath("address").description("??????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.updatedDeliveryId").description("????????? ?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void delivery() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(put(API_DELIVERY, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-delivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void complete() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(put(API_COMPLETE, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-complete",
                        pathParameters(
                                parameterWithName("deliveryId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void cancel() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(put(API_CANCEL, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-cancel",
                        pathParameters(
                                parameterWithName("deliveryId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void deleteDelivery() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_DELIVERY, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-deleteDelivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }
}