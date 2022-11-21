package com.jeeok.jeeokmember.common.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

    private String zipcode;
    private String address;

    public String fullAddress() {
        return "(" + this.zipcode + ") " + this.address;
    }
}
