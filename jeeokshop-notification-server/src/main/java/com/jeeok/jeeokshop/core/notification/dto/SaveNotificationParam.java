package com.jeeok.jeeokshop.core.notification.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveNotificationParam {

    private Long memberId;
    private Long itemId;
    private String title;
    private String message;
}
