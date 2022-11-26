package com.jeeok.jeeokmember.common.exception;

import com.jeeok.jeeokmember.common.json.JsonError;
import com.jeeok.jeeokmember.common.json.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResult> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<JsonError> errors = e.getFieldErrors().stream()
                .map(error -> JsonError.builder()
                        .fieldName(error.getField())
                        .errorMessage(messageSource.getMessage(error.getCodes()[1], error.getArguments(), request.getLocale()))
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(JsonResult.ERROR(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors));
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * @RequestParam eunm 으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<JsonResult> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        System.out.println("e.getName() = " + e.getName());
        List<JsonError> errors = List.of(
                JsonError.builder()
                        .fieldName(e.getName())
                        .errorMessage(messageSource.getMessage("enum.type.mismatch", null, request.getLocale()))
                        .build()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(JsonResult.ERROR(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors));
    }

    /**
     * custom errors
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<JsonResult> commonExceptionHandler(CommonException e, HttpServletRequest request) {
        List<JsonError> jsonErrors = e.getErrorCodes().stream()
                .map(code -> JsonError.builder()
                        .fieldName(code.getFieldName())
                        .errorMessage(messageSource.getMessage(code.getErrorCode(), code.getArguments(), request.getLocale()))
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(JsonResult.ERROR(e.getMessage(), jsonErrors));
    }
}
