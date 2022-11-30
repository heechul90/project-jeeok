package com.jeeok.apigatewayserver.filter;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            //Netty 비동기 방식 서버 사용시에는 ServerHttpRequest 를 사용해야 한다.
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global baseMessage: {}", config.getBaseMessage());

            // Global pre Filter
            if (config.isPreLogger()){
                log.info("[GlobalFilter Start] request ID = {}, method = {}, path = {}", request.getId(), request.getMethod(), request.getPath());
            }

            //Global Post Filter
            //비동기 방식의 단일값 전달시 Mono  사용(Webflux)
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()){
                    log.info("[GlobalFilter End] request ID = {}, method = {}, path = {}, statusCode = {}", request.getId(), request.getMethod(), request.getPath(), response.getStatusCode());

                }
            }));
        };
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
