package com.jeeok.memberserver.core.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {

    @Column(name = "phoneNumberFirst", length = 3)
    private String first;
    @Column(name = "phoneNumberMiddle", length = 4)
    private String middle;
    @Column(name = "phoneNumberLast", length = 4)
    private String last;

    public PhoneNumber(String first, String middle, String last) {
        this.first = first;
        this.middle = middle;
        this.last = last;
    }

    public String fullPhoneNumber() {
        return this.first + this.middle + this.last;
    }
}
