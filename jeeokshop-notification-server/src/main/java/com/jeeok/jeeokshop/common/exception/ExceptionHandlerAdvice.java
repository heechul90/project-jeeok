package com.jeeok.jeeokshop.common.exception;

import com.jeeok.jeeokshop.common.exception.dto.ErrorCode;
import com.jeeok.jeeokshop.common.exception.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {

    private final MessageSource messageSource;

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(), messageSource);
        return Mono.just(response);
    }

    /**
     * 바인딩 객체 @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(), messageSource);
        return Mono.just(response);
    }

    /**
     * 요청은 잘 만들어졌지만, 문법 오류로 인하여 따를 수 없을때 발생한다.
     */
    @ExceptionHandler(HttpClientErrorException.UnprocessableEntity.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ErrorResponse> handleUnprocessableEntityException(HttpClientErrorException.UnprocessableEntity e) {
        log.error("handleUnprocessableEntityException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNPROCESSABLE_ENTITY, messageSource);
        return Mono.just(response);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @requestParam enum 으로 binding 못했을 때 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        ErrorResponse response = ErrorResponse.of(e, messageSource);
        return Mono.just(response);
    }

    /**
     * 요청한 페이지가 존재하지 않는 경우 발생
     */


    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected Mono<ResponseEntity<ErrorResponse>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ACCESS_DENIED, messageSource);
        return Mono.just(ResponseEntity.status(HttpStatus.valueOf(ErrorCode.ACCESS_DENIED.getStatus()))
                .body(response));
    }

    /**
     * 사용자 인증되지 않은 경우 발생
     */
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    protected Mono<ResponseEntity<ErrorResponse>> handleUnauthorizedException(HttpClientErrorException.Unauthorized e) {
        log.error("handleUnauthorizedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED, messageSource);
        return Mono.just(ResponseEntity.status(HttpStatus.valueOf(ErrorCode.ACCESS_DENIED.getStatus()))
                .body(response));
    }

    /**
     * JWT 인증 만료 경우 발생
     */

    /**
     * 사용자에게 표시할 다양한 메시지를 직접 정의하여 처리하는 Business RuntimeException Handler
     * 개발자가 만들어 던지는 런타임 오류를 처리
     */
    @ExceptionHandler(BusinessMessageException.class)
    protected Mono<ResponseEntity<ErrorResponse>> handleBusinessMessageException(BusinessMessageException e) {
        log.error("handleBusinessMessageException", e);
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode(), e.getCustomMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getStatus()))
                .body(response));
    }

    /**
     * 개발자가 정의 ErrorCode 를 처리하는 Business RuntimeException Handler
     * 개발자가 만들어 던지는 런타임 오류를 처리
     */
    @ExceptionHandler(BusinessException.class)
    protected Mono<ResponseEntity<ErrorResponse>> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode(), messageSource);
        return Mono.just(ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getStatus()))
                .body(response));
    }

    /**
     * javax.validation.Valid or @Validated 로 binding error 발생시 발생한다.
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Mono<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException e) {
        log.error("handleWebExchangeBindException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult(), messageSource);
        return Mono.just(response);
    }

    /**
     * default exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected Mono<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, messageSource);
        return Mono.just(response);
    }
}
