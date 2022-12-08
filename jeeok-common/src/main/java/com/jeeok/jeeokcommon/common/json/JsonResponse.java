package com.jeeok.jeeokcommon.common.json;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JsonResponse<T> {

    private LocalDateTime transaction_time;
    private String message;
    private int status;
    private Code code;

    @Nullable
    private T data;

    public static <T> JsonResponse<T> OK() {
        return (JsonResponse<T>) JsonResponse.builder()
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .code(Code.SUCCESS)
                .build();
    }

    public static <T> JsonResponse<T> OK(T data) {
        return (JsonResponse<T>) JsonResponse.builder()
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .code(Code.SUCCESS)
                .data(data)
                .build();
    }
}
