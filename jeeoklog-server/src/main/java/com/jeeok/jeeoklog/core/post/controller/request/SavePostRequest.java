package com.jeeok.jeeoklog.core.post.controller.request;

import com.jeeok.jeeoklog.common.exception.JsonInvalidRequest;
import com.jeeok.jeeoklog.common.json.ErrorCode;
import com.jeeok.jeeoklog.core.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavePostRequest {

    private String postTitle;
    private String postContent;

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public Post toPost() {
        return Post.createPost()
                .title(this.postTitle)
                .content(this.postContent)
                .build();
    }
}
