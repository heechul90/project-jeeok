package com.jeeok.jeeokshop.common.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

    /** 등록자 */
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    /** 수정자 */
    @LastModifiedBy
    private String lastModifiedBy;
}
