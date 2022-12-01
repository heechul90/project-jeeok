package com.jeeok.jeeokshop.common.exception;

import com.jeeok.jeeokshop.common.json.ErrorResponse;
import com.jeeok.jeeokshop.common.json.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResult> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<ErrorResponse> errors = e.getFieldErrors().stream()
                .map(error -> ErrorResponse.builder()
                        .fieldName(error.getField())
                        .errorMessage(messageSource.getMessage(error.getCodes()[1], error.getArguments(), request.getLocale()))
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(JsonResult.ERROR(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<JsonResult> businessExceptionHandler(BusinessException e, HttpServletRequest request) {
        List<ErrorResponse> errors = e.getErrorCodes().stream()
                .map(error -> ErrorResponse.builder()
                        .fieldName(error.getFieldName())
                        .errorMessage(messageSource.getMessage(error.getErrorCode(), error.getArguments(), request.getLocale()))
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity
                .status(e.httpStatus())
                .body(JsonResult.ERROR(e.getMessage(), errors));
    }
}
