package com.jeeok.jeeoklog.client.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeoklog.common.json.JsonResult;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = null;
        if (response.body() != null) {
            try {
                JsonResult jsonResult = objectMapper.readValue(response.body().asInputStream(), JsonResult.class);
                message = jsonResult.getMessage();
            } catch (IOException e) {
                String catchErrorMessage = "Error Deserializing response body from failed feign request response.";
                log.warn(methodKey + catchErrorMessage, e);
                return new FeignClientException("고객센터로 문의해주세요.");
            }
        }

        return new FeignClientException(message);
    }
}
