package com.jeeok.jeeokshop.common.entity;

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
public class Photo {

    @Column(name = "photo_path")
    private String path;

    @Column(name = "photo_name")
    private String name;

    public String path() {
        return this.path + "/" + this.name;
    }
}
