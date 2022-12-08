package com.jeeok.jeeokshop.common.json;

import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JsonResult<T> {

    private LocalDateTime transaction_time;
    private Code code;
    private String message;

    @Nullable
    private T data;

    public static <T> JsonResult<T> OK() {
        return (JsonResult<T>) JsonResult.builder()
                .transaction_time(LocalDateTime.now())
                .code(Code.SUCCESS)
                .build();
    }

    public static <T> JsonResult<T> OK(T data) {
        return (JsonResult<T>) JsonResult.builder()
                .transaction_time(LocalDateTime.now())
                .code(Code.SUCCESS)
                .data(data)
                .build();
    }
}
