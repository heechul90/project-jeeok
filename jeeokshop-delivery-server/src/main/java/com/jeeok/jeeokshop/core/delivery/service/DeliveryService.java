package com.jeeok.jeeokshop.core.delivery.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.dto.DeliverySearchCondition;
import com.jeeok.jeeokshop.core.delivery.dto.UpdateDeliveryParam;
import com.jeeok.jeeokshop.core.delivery.repository.DeliveryQueryRepository;
import com.jeeok.jeeokshop.core.delivery.repository.DeliveryRepository;
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
public class DeliveryService {

    public static final String DELIVERY = "Delivery";

    private final DeliveryQueryRepository deliveryQueryRepository;
    private final DeliveryRepository deliveryRepository;

    /**
     * 배송 목록 조회
     */
    public Page<Delivery> findDeliveries(DeliverySearchCondition condition, Pageable pageable) {
        return deliveryQueryRepository.findDeliveries(condition, pageable);
    }

    /**
     * 배송 단건 조회
     */
    public Delivery findDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFound(DELIVERY, deliveryId));
    }

    /**
     * 배송 저장
     */
    @Transactional
    public Long saveDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery).getId();
    }

    /**
     * 배송 수정
     */
    @Transactional
    public void updateDelivery(Long deliveryId, UpdateDeliveryParam param) {
        Delivery findDelivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFound(DELIVERY, deliveryId));
        findDelivery.updateDelivery(param);
    }

    /**
     * 배송 시작
     */
    @Transactional
    public void delivery(Long deliveryId) {
        Delivery findDelivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFound(DELIVERY, deliveryId));
        findDelivery.delivery();
    }

    /**
     * 배송 완료
     */
    @Transactional
    public void complete(Long deliveryId) {
        Delivery findDelivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFound(DELIVERY, deliveryId));
        findDelivery.complete();
    }

    /**
     * 배달취소
     */
    @Transactional
    public void cancel(Long memberId, Long orderId) {
        Delivery findDelivery = deliveryRepository.findByMemberIdAndOrderId(memberId, orderId)
                .orElseThrow(() -> new IllegalArgumentException("없음."));
        findDelivery.cancel();
    }

    /**
     * 배송 삭제
     */
    @Transactional
    public void deleteDelivery(Long deliveryId) {
        Delivery findDelivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFound(DELIVERY, deliveryId));
        deliveryRepository.delete(findDelivery);
    }
}
