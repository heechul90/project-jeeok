package com.jeeok.jeeokshop.core.deliveryRider.service;

import com.jeeok.jeeokshop.client.member.FindMemberResponse;
import com.jeeok.jeeokshop.client.member.MemberClient;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.PhoneNumber;
import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.delivery.repository.DeliveryRepository;
import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import com.jeeok.jeeokshop.core.deliveryRider.dto.SaveDeliveryRiderParam;
import com.jeeok.jeeokshop.core.deliveryRider.dto.UpdateDeliveryRiderParam;
import com.jeeok.jeeokshop.core.deliveryRider.repository.DeliveryRiderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeliveryRiderServiceTest extends MockTest {

    //CREATE_DELIVERY_RIDER
    public static final Address DELIVERY_ASSRESS = new Address("00283", "서울시");
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final Long RIDER_ID_1 = 7L;
    public static final String RIDER_NAME = "rider_name";
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "2203", "3838");
    public static final Long DELIVERY_RIDER_ID_1 = 1L;
    public static final Long DELIVERY_ID_1 = 1L;

    //UPDATE_DELIVERY_RIDER
    public static final String UPDATE_RIDER_NAME = "update_rider_name";
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "3332", "9444");

    //FIND_RIDER
    public static final String EMAIL = "email";
    public static final String ROLE = "라이더";
    public static final String AUTH = "JEEOK";
    public static final Address RIDER_ADDRESS = new Address("88378", "세종시");

    //ERROR_MESSAGE
    public static final String DELIVERY = "Delivery";
    public static final String DELIVERY_RIDER = "DeliveryRider";
    public static final Long NOT_FOUND_DELIVERY_ID_0 = 0L;
    public static final Long NOT_FOUND_DELIVERY_RIDER_ID_0 = 0L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    @Mock protected DeliveryRiderRepository deliveryRiderRepository;
    @Mock protected DeliveryRepository deliveryRepository;
    @Mock protected MemberClient memberClient;
    @InjectMocks protected DeliveryRiderService deliveryRiderService;

    Delivery delivery;
    DeliveryRider deliveryRider;

    @BeforeEach
    void beforeEach() {
        delivery = Delivery.createDelivery()
                .address(DELIVERY_ASSRESS)
                .memberId(MEMBER_ID_1)
                .orderId(ORDER_ID_1)
                .build();

        deliveryRider = DeliveryRider.createDeliveryRider()
                .riderId(RIDER_ID_1)
                .riderName(RIDER_NAME)
                .phoneNumber(PHONE_NUMBER)
                .delivery(delivery)
                .build();
    }

    @Nested
    class SuccessfulTest {

        @Test
        @DisplayName("배송 라이더 단건 조회")
        void findDeliveryRider() {
            //given
            given(deliveryRiderRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(deliveryRider));

            //when
            DeliveryRider findDeliveryRider = deliveryRiderService.findDeliveryRider(DELIVERY_RIDER_ID_1);

            //then
            assertThat(findDeliveryRider.getRiderId()).isEqualTo(RIDER_ID_1);
            assertThat(findDeliveryRider.getRiderName()).isEqualTo(RIDER_NAME);
            assertThat(findDeliveryRider.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
            assertThat(findDeliveryRider.getDelivery()).isEqualTo(delivery);

            //verify
            verify(deliveryRiderRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 라이더 저장")
        void saveDeliveryRider() {
            //given
            FindMemberResponse findRider = FindMemberResponse.builder()
                    .memberId(RIDER_ID_1)
                    .email(EMAIL)
                    .memberName(RIDER_NAME)
                    .role(ROLE)
                    .auth(AUTH)
                    .phoneNumber(PHONE_NUMBER.fullPhoneNumber())
                    .address(RIDER_ADDRESS)
                    .build();

            given(memberClient.findMember(any(Long.class))).willReturn(JsonResult.OK(findRider));
            given(deliveryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(delivery));
            given(deliveryRiderRepository.save(any(DeliveryRider.class))).willReturn(deliveryRider);

            SaveDeliveryRiderParam param = SaveDeliveryRiderParam.builder()
                    .deliveryId(DELIVERY_ID_1)
                    .build();

            //when
            DeliveryRider savedDeliveryRider = deliveryRiderService.saveDeliveryRider(param, RIDER_ID_1);

            //then
            assertThat(savedDeliveryRider.getRiderId()).isEqualTo(RIDER_ID_1);
            assertThat(savedDeliveryRider.getRiderName()).isEqualTo(RIDER_NAME);
            assertThat(savedDeliveryRider.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
            assertThat(savedDeliveryRider.getDelivery()).isEqualTo(delivery);

            //verify
            verify(memberClient, times(1)).findMember(any(Long.class));
            verify(deliveryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 라이더 수정")
        void updateDeliveryRider() {
            //given
            given(deliveryRiderRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(deliveryRider));

            UpdateDeliveryRiderParam param = UpdateDeliveryRiderParam.builder()
                    .riderName(UPDATE_RIDER_NAME)
                    .phoneNumber(UPDATE_PHONE_NUMBER)
                    .build();

            //when
            deliveryRiderService.updateDeliveryRider(DELIVERY_RIDER_ID_1, param);

            //then
            assertThat(deliveryRider.getRiderName()).isEqualTo(UPDATE_RIDER_NAME);
            assertThat(deliveryRider.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);

            //verify
            verify(deliveryRiderRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 라이더 삭제")
        void deleteDeliveryRider() {
            //given
            given(deliveryRiderRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(deliveryRider));

            //when
            deliveryRiderService.deleteDeliveryRider(DELIVERY_RIDER_ID_1);

            //then
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.CANCEL);

            //verify
            verify(deliveryRiderRepository, times(1)).findById(any(Long.class));
            verify(deliveryRiderRepository, times(1)).delete(any(DeliveryRider.class));
        }
    }

    @Nested
    class EntityNotFoundTest {

        @Test
        @DisplayName("배송 라이더 단건 조회_예외")
        void findDeliveryRider_exception() {
            //given
            given(deliveryRiderRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY_RIDER, NOT_FOUND_DELIVERY_RIDER_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryRiderService.findDeliveryRider(NOT_FOUND_DELIVERY_RIDER_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY_RIDER)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_ID_0);

            //verify
            verify(deliveryRiderRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 라이더 저장_예외")
        void saveDeliveryRider_exception() {
            //given
            FindMemberResponse findRider = FindMemberResponse.builder()
                    .memberId(RIDER_ID_1)
                    .email(EMAIL)
                    .memberName(RIDER_NAME)
                    .role(ROLE)
                    .auth(AUTH)
                    .phoneNumber(PHONE_NUMBER.fullPhoneNumber())
                    .address(RIDER_ADDRESS)
                    .build();

            given(memberClient.findMember(any(Long.class))).willReturn(JsonResult.OK(findRider));
            given(deliveryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY, NOT_FOUND_DELIVERY_ID_0));

            SaveDeliveryRiderParam param = SaveDeliveryRiderParam.builder()
                    .deliveryId(NOT_FOUND_DELIVERY_ID_0)
                    .build();

            //expected
            assertThatThrownBy(() -> deliveryRiderService.saveDeliveryRider(param, NOT_FOUND_DELIVERY_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_ID_0);

            //verify
            verify(memberClient, times(1)).findMember(any(Long.class));
            verify(deliveryRepository, times(1)).findById(any(Long.class));
            verify(deliveryRiderRepository, times(0)).save(any(DeliveryRider.class));
        }

        @Test
        @DisplayName("배송 라이더 수정_예외")
        void updateDeliveryRider_exception() {
            //given
            given(deliveryRiderRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY_RIDER, NOT_FOUND_DELIVERY_RIDER_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryRiderService.updateDeliveryRider(0L, any(UpdateDeliveryRiderParam.class)))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY_RIDER)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_RIDER_ID_0);

            //verify
            verify(deliveryRiderRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 라이더 삭제_예외")
        void deleteDeliveryRider_exception() {
            //given
            given(deliveryRiderRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY_RIDER, NOT_FOUND_DELIVERY_RIDER_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryRiderService.deleteDeliveryRider(NOT_FOUND_DELIVERY_RIDER_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY_RIDER)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_RIDER_ID_0);

            //verify
            verify(deliveryRiderRepository, times(1)).findById(any(Long.class));
            verify(deliveryRiderRepository, times(0)).delete(any(DeliveryRider.class));
        }
    }
}