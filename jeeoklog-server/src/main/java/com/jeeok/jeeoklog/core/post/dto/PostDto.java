package com.jeeok.jeeoklog.core.post.dto;

import com.jeeok.jeeoklog.core.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {

    private Long postId;
    private String postTitle;
    private String postContent;
    private int hits;
    private LocalDateTime createdDate;
    private String createdBy;

    public PostDto(Post post) {
        this.postId = post.getId();
        this.postTitle = post.getTitle();
        this.postContent = post.getContent();
        this.hits = post.getHits();
        this.createdDate = post.getCreatedDate();
        this.createdBy = post.getCreatedBy();
    }
}
