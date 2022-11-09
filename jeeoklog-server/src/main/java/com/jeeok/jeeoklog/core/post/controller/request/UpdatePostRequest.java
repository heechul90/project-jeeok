package com.jeeok.jeeoklog.core.post.controller.request;

import com.jeeok.jeeoklog.common.exception.JsonInvalidRequest;
import com.jeeok.jeeoklog.common.json.ErrorCode;
import com.jeeok.jeeoklog.core.post.dto.UpdatePostParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdatePostRequest {

    private String postTitle;
    private String postContent;

    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public UpdatePostParam toParam() {
        return UpdatePostParam.builder()
                .title(this.postTitle)
                .content(this.postContent)
                .build();
    }
}
