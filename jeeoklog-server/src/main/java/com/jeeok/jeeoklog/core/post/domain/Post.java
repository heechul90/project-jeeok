package com.jeeok.jeeoklog.core.post.domain;

import com.jeeok.jeeoklog.common.entity.BaseEntity;
import com.jeeok.jeeoklog.core.post.dto.UpdatePostParam;
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

    private int hits;

    //===생성===//
    /** 게시물 생성 */
    @Builder(builderMethodName = "createPost")
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
        this.hits = 0;
    }

    //===수정===//
    public void updatePost(UpdatePostParam param) {
        this.title = param.getTitle();
        this.content = param.getContent();
    }

    /** 조회수 증가 */
    public void hit() {
        this.hits += 1;
    }
}
