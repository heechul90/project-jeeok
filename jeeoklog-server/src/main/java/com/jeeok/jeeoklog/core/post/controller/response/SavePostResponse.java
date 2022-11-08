package com.jeeok.jeeoklog.core.post.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SavePostResponse {

    private Long savedPostId;
}
