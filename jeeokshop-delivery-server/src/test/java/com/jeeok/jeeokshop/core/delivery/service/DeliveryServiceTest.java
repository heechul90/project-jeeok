package com.jeeok.jeeokshop.core.delivery.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.delivery.domain.Address;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.delivery.dto.UpdateDeliveryParam;
import com.jeeok.jeeokshop.core.delivery.exception.OrderNotFound;
import com.jeeok.jeeokshop.core.delivery.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeliveryServiceTest extends MockTest {

    //CREATE_DELIVERY
    public static final Long DELIVERY_ID_1 = 1L;
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final Address ADDRESS = new Address("38273", "서울시");

    //UPDATE_DELIVERY
    public static final Address UPDATE_ADDRESS = new Address("33871", "세종시");

    //ERROR_MESSAGE
    public static final String DELIVERY = "Delivery";
    public static final Long NOT_FOUND_DELIVERY_ID_0 = 0L;
    public static final Long NOT_FOUND_MEMBER_ID_0 = 0L;
    public static final Long NOT_FOUND_ORDER_ID_0 = 0L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    private Delivery getDelivery(Address address, Long memberId, Long orderId) {
        return Delivery.createDelivery()
                .address(address)
                .memberId(memberId)
                .orderId(orderId)
                .build();
    }

    @Mock protected DeliveryRepository deliveryRepository;

    @InjectMocks protected DeliveryService deliveryService;

    Delivery delivery;

    @BeforeEach
    void beforeEach() {
        delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
    }

    @Nested
    class SuccessfulTest {
        @Test
        @DisplayName("배송 단건 조회")
        void findDelivery() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(delivery));

            //when
            Delivery findDelivery = deliveryService.findDelivery(DELIVERY_ID_1);

            //then
            assertThat(findDelivery.getAddress()).isEqualTo(ADDRESS);
            assertThat(findDelivery.getMemberId()).isEqualTo(MEMBER_ID_1);
            assertThat(findDelivery.getOrderId()).isEqualTo(ORDER_ID_1);

            //verify
            verify(deliveryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 저장")
        void saveDelivery() {
            //given
            given(deliveryRepository.save(any(Delivery.class))).willReturn(delivery);

            //when
            Delivery savedDelivery = deliveryService.saveDelivery(DeliveryServiceTest.this.delivery);

            //then
            assertThat(savedDelivery.getAddress()).isEqualTo(ADDRESS);
            assertThat(savedDelivery.getMemberId()).isEqualTo(MEMBER_ID_1);
            assertThat(savedDelivery.getOrderId()).isEqualTo(ORDER_ID_1);

            //verify
            verify(deliveryRepository, times(1)).save(any(Delivery.class));
        }

        @Test
        @DisplayName("배송 수정")
        void updateDelivery() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(delivery));

            UpdateDeliveryParam param = UpdateDeliveryParam.builder()
                    .address(UPDATE_ADDRESS)
                    .build();

            //when
            deliveryService.updateDelivery(DELIVERY_ID_1, param);

            //then
            assertThat(delivery.getAddress()).isEqualTo(UPDATE_ADDRESS);

            //verify
            verify(deliveryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 시작")
        void delivery() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(delivery));

            //when
            deliveryService.delivery(DELIVERY_ID_1);

            //then
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DELIVERY);

            //verify
            verify(deliveryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 완료")
        void complete() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(delivery));

            //when
            deliveryService.complete(DELIVERY_ID_1);

            //then
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.COMPLETE);

            //verify
            verify(deliveryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 취소 byDeliveryId")
        void cancelByDeliveryId() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(delivery));

            //when
            deliveryService.cancelByDeliveryId(DELIVERY_ID_1);

            //then
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.CANCEL);

            //verify
            verify(deliveryRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("배송 취소 byMemberIdAndOrderId")
        void cancelByMemberIdAndOrderId() {
            //given
            given(deliveryRepository.findByMemberIdAndOrderId(any(Long.class), any(Long.class))).willReturn(Optional.ofNullable(delivery));

            //when
            deliveryService.cancelByMemberIdAndOrderId(MEMBER_ID_1, ORDER_ID_1);

            //then
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.CANCEL);

            //verify
            verify(deliveryRepository, times(1)).findByMemberIdAndOrderId(any(Long.class), any(Long.class));
        }

        @Test
        @DisplayName("배송 삭제")
        void deleteDelivery() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(delivery));

            //when
            deliveryService.deleteDelivery(DELIVERY_ID_1);

            //then

            //verify
            verify(deliveryRepository, times(1)).findById(any(Long.class));
            verify(deliveryRepository, times(1)).delete(any(Delivery.class));
        }
    }

    @Nested
    class ExceptionTest {

        @Test
        @DisplayName("배송 단건 조회_entityNotFound")
        void findDelivery_entityNotFound() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY, NOT_FOUND_DELIVERY_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryService.findDelivery(NOT_FOUND_DELIVERY_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_ID_0);
        }
        
        @Test
        @DisplayName("배송 수정_entityNotFound")
        void updateDelivery_entityNotFound() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY, NOT_FOUND_DELIVERY_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryService.updateDelivery(NOT_FOUND_DELIVERY_ID_0, any(UpdateDeliveryParam.class)))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_ID_0);
        }

        @Test
        @DisplayName("배송 시작_entityNotFound")
        void delivery_entityNotFound() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY, NOT_FOUND_DELIVERY_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryService.delivery(NOT_FOUND_DELIVERY_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_ID_0);
        }

        @Test
        @DisplayName("배송 완료_entityNotFound")
        void complete_entityNotFound() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY, NOT_FOUND_DELIVERY_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryService.complete(NOT_FOUND_DELIVERY_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_ID_0);
        }

        @Test
        @DisplayName("배달 취소 by deliveryId_entityNotFound")
        void cancelByDeliveryId_entityNotFound() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY, NOT_FOUND_DELIVERY_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryService.cancelByDeliveryId(NOT_FOUND_DELIVERY_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_ID_0);
        }

        @Test
        @DisplayName("배달 취소 by memberId and orderId_orderNotFound")
        void cancelByMemberIdAndOrderId_orderNotFound() {
            //given
            given(deliveryRepository.findByMemberIdAndOrderId(any(Long.class), any(Long.class))).willThrow(new OrderNotFound());

            //expected
            assertThatThrownBy(() -> deliveryService.cancelByMemberIdAndOrderId(NOT_FOUND_MEMBER_ID_0, NOT_FOUND_ORDER_ID_0))
                    .isInstanceOf(OrderNotFound.class)
                    .hasMessageStartingWith("주문내역")
                    .hasMessageEndingWith("없습니다.");
        }

        @Test
        @DisplayName("배송 삭제_entityNotFound")
        void deleteDelivery_entityNotFound() {
            //given
            given(deliveryRepository.findById(any(Long.class))).willThrow(new EntityNotFound(DELIVERY, NOT_FOUND_DELIVERY_ID_0));

            //expected
            assertThatThrownBy(() -> deliveryService.deleteDelivery(NOT_FOUND_DELIVERY_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + DELIVERY)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_DELIVERY_ID_0);
        }
    }
}