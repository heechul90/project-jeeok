package com.jeeok.jeeokcommon.common.exception.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldError {

    private String message;
    private String field;
    private String rejectedValue;

    @Builder
    private FieldError(final String field, final String rejectedValue, final String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public static List<FieldError> of(final String field, final String rejectedValue, final String message) {
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError(field, rejectedValue, message));
        return fieldErrors;
    }

    /**
     * BindingResult to FieldError
     */
    public static List<FieldError> of(final BindingResult bindingResult) {
        final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(error -> FieldError.builder()
                        .field(error.getField())
                        .rejectedValue(error.getRejectedValue() == null ? "" : error.getRejectedValue().toString())
                        .message(error.getDefaultMessage())
                        .build()
                ).collect(Collectors.toList());
    }
}
