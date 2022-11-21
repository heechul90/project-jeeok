package com.jeeok.jeeokshop.core.deliveryRider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.PhoneNumber;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.deliveryRider.controller.request.SaveDeliveryRiderRequest;
import com.jeeok.jeeokshop.core.deliveryRider.controller.request.UpdateDeliveryRiderRequest;
import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import com.jeeok.jeeokshop.core.deliveryRider.service.DeliveryRiderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminDeliveryRiderControllerTest extends IntegrationTest {

    //CREATE_DELIVERY_RIDER
    public static final Long RIDER_ID_100 = 5L;
    public static final String RIDER_NAME = "홍길동";
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "8922", "1189");
    public static final Address ADDRESS = new Address("88372", "서울시");
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;

    //UPDATE_DELIVERY_RIDER
    public static final String UPDATE_RIDER_NAME = "채치수";
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "8883", "1100");

    //REQUEST_INFO
    public static final String API_SAVE_DELIVERY_RIDER = "/admin/deliveryRiders";
    public static final String API_UPDATE_DELIVERY_RIDER = "/admin/deliveryRiders/{deliveryRiderId}";
    public static final String API_DELETE_DELIVERY_RIDER = "/admin/deliveryRiders/{deliveryRiderId}";

    @PersistenceContext protected EntityManager em;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected DeliveryRiderService deliveryRiderService;

    private DeliveryRider getDeliveryRider(Long riderId, String riderName, PhoneNumber phoneNumber, Delivery delivery) {
        return DeliveryRider.createDeliveryRider()
                .riderId(riderId)
                .riderName(riderName)
                .phoneNumber(phoneNumber)
                .delivery(delivery)
                .build();
    }

    @Test
    @DisplayName("배송 라이더 저장")
    void saveDeliveryRider() throws Exception {
        //given
        Delivery delivery = new Delivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        SaveDeliveryRiderRequest request = SaveDeliveryRiderRequest.builder()
                .deliveryId(delivery.getId())
                .riderId(RIDER_ID_100)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_SAVE_DELIVERY_RIDER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-saveDeliveryRider",
                        requestFields(
                                fieldWithPath("deliveryId").description("배송 고유번호"),
                                fieldWithPath("riderId").description("라이더 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.savedDeliveryRiderId").description("저장된 배송 라이더 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("배송 라이더 수정")
    void updateDeliveryRider() throws Exception {
        //given
        Delivery delivery = new Delivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        DeliveryRider deliveryRider = getDeliveryRider(RIDER_ID_100, RIDER_NAME, PHONE_NUMBER, delivery);
        em.persist(delivery);
        em.persist(deliveryRider);

        UpdateDeliveryRiderRequest request = UpdateDeliveryRiderRequest.builder()
                .riderName(UPDATE_RIDER_NAME)
                .phoneNumber(UPDATE_PHONE_NUMBER.fullPhoneNumber())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_UPDATE_DELIVERY_RIDER, deliveryRider.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-updateDeliveryRider",
                        pathParameters(
                                parameterWithName("deliveryRiderId").description("배송 라이더 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("riderName").description("라이더 이름"),
                                fieldWithPath("phoneNumber").description("휴대폰번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.updatedDeliveryRiderId").description("수정된 배송 라이더 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("배송 라이더 삭제")
    void deleteDeliveryRider() throws Exception {
        //given
        Delivery delivery = new Delivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        DeliveryRider deliveryRider = getDeliveryRider(RIDER_ID_100, RIDER_NAME, PHONE_NUMBER, delivery);
        em.persist(delivery);
        em.persist(deliveryRider);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_DELIVERY_RIDER, deliveryRider.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-deleteDeliveryRider",
                        pathParameters(
                                parameterWithName("deliveryRiderId").description("배송 라이더 고유번호")
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