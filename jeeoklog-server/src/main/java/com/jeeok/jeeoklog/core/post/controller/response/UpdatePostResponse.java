package com.jeeok.jeeoklog.core.post.controller.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdatePostResponse {

    private Long updatedPostId;
}
