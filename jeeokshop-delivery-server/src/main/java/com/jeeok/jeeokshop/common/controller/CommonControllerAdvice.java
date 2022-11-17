package com.jeeok.jeeokshop.common.controller;

import com.jeeok.jeeokshop.common.exception.CommonException;
import com.jeeok.jeeokshop.common.json.JsonError;
import com.jeeok.jeeokshop.common.json.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {

    private final MessageSource messageSource;

    /**
     * bindingResult erorrs
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<JsonError> errors = e.getFieldErrors().stream()
                .map(error -> new JsonError(error.getField(), messageSource.getMessage(error.getCodes()[0], error.getArguments(), request.getLocale())))
                .collect(Collectors.toList());
        return JsonResult.ERROR(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    }

    /**
     * custom errors
     */
    @ExceptionHandler(CommonException.class)
    public JsonResult commonExceptionHandler(CommonException e, HttpServletRequest request) {
        List<JsonError> jsonErrors = e.getErrorCodes().stream()
                .map(code -> new JsonError(code.getFieldName(), messageSource.getMessage(code.getErrorCode(), code.getArguments(), request.getLocale())))
                .collect(Collectors.toList());
        return JsonResult.ERROR(e.getMessage(), jsonErrors);
    }
}
