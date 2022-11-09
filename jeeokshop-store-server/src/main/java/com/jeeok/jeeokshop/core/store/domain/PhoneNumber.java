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
public class PhoneNumber {

    @Column(name = "phoneNumberFirst", length = 3)
    private String first;
    @Column(name = "phoneNumberMiddle", length = 4)
    private String middle;
    @Column(name = "phoneNumberLast", length = 4)
    private String last;

    public String fullPhoneNumber() {
        return this.first + this.middle + this.last;
    }
}
