package com.jeeok.jeeokshop.core.deliveryRider.service;

import com.jeeok.jeeokshop.client.member.FindMemberResponse;
import com.jeeok.jeeokshop.client.member.MemberClient;
import com.jeeok.jeeokshop.common.entity.PhoneNumber;
import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.repository.DeliveryRepository;
import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import com.jeeok.jeeokshop.core.deliveryRider.dto.DeliveryRiderSearchCondition;
import com.jeeok.jeeokshop.core.deliveryRider.dto.SaveDeliveryRiderParam;
import com.jeeok.jeeokshop.core.deliveryRider.dto.UpdateDeliveryRiderParam;
import com.jeeok.jeeokshop.core.deliveryRider.repository.DeliveryRiderQueryRepository;
import com.jeeok.jeeokshop.core.deliveryRider.repository.DeliveryRiderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryRiderService {

    public static final String DELIVERY_RIDER = "DeliveryRider";
    public static final String DELIVERY = "Delivery";

    private final DeliveryRiderQueryRepository deliveryRiderQueryRepository;
    private final DeliveryRiderRepository deliveryRiderRepository;
    private final DeliveryRepository deliveryRepository;

    private final MemberClient memberClient;

    /**
     * 배송 라이더 목록 조회
     */
    public Page<DeliveryRider> findDeliveryRiders(DeliveryRiderSearchCondition condition, Pageable pageable) {
        return deliveryRiderQueryRepository.findDeliveryRiders(condition, pageable);
    }

    /**
     * 배송 라이더 단건 조회
     */
    public DeliveryRider findDeliveryRider(Long deliveryRiderId) {
        return deliveryRiderRepository.findById(deliveryRiderId)
                .orElseThrow(() -> new EntityNotFound(DELIVERY_RIDER, deliveryRiderId));
    }

    /**
     * 배송 라이더 저장
     */
    @Transactional
    public DeliveryRider saveDeliveryRider(SaveDeliveryRiderParam param, Long riderId) {
        //라이더 정보
        FindMemberResponse findRider = memberClient.findMember(riderId).getData();

        //배송 정보
        Delivery findDelivery = deliveryRepository.findById(param.getDeliveryId())
                .orElseThrow(() -> new EntityNotFound(DELIVERY, param.getDeliveryId()));

        DeliveryRider deliveryRider = DeliveryRider.createDeliveryRider()
                .riderId(riderId)
                .riderName(findRider.getMemberName())
                .phoneNumber(new PhoneNumber(findRider.getPhoneNumber().substring(0, 3), findRider.getPhoneNumber().substring(3, 7), findRider.getPhoneNumber().substring(7, 11)))
                .delivery(findDelivery)
                .build();

        //배송 상태 업데이트
        findDelivery.delivery();

        return deliveryRiderRepository.save(deliveryRider);
    }

    /**
     * 배송 라이더 수정
     */
    @Transactional
    public void updateDeliveryRider(Long deliveryRiderId, UpdateDeliveryRiderParam param) {
        DeliveryRider findDeliveryRider = deliveryRiderRepository.findById(deliveryRiderId)
                .orElseThrow(() -> new EntityNotFound(DELIVERY_RIDER, deliveryRiderId));
        findDeliveryRider.updateDeliveryRider(param);
    }

    /**
     * 배송 라이더 삭제
     */
    @Transactional
    public void deleteDeliveryRider(Long deliveryRiderId) {
        DeliveryRider findDeliveryRider = deliveryRiderRepository.findById(deliveryRiderId)
                .orElseThrow(() -> new EntityNotFound(DELIVERY_RIDER, deliveryRiderId));

        //배송 상태 업데이트(취소로 업데이트)
        findDeliveryRider.getDelivery().cancel();

        //삭제
        deliveryRiderRepository.delete(findDeliveryRider);
    }
}
