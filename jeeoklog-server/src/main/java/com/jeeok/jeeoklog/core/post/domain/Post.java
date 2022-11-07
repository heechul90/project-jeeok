package com.jeeok.jeeoklog.core.post.domain;

import com.jeeok.jeeoklog.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    //===생성 매서드===//
    /** 게시물 생성 */
    @Builder(builderMethodName = "createPost")
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
