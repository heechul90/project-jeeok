package com.jeeok.jeeokshop.core.store.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BusinessHours {

    @Column(name = "business_opening_hours", columnDefinition = "char(4)")
    private String openingHours;

    @Column(name = "business_closing_hours", columnDefinition = "char(4)")
    private String closingHours;
}
