package com.jeeok.apigatewayserver.filter;

import com.jeeok.apigatewayserver.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * /api/front/** check filter
 */
@Slf4j
@Component
public class ManagerAuthorizationHeaderFilter extends AbstractGatewayFilterFactory<ManagerAuthorizationHeaderFilter.Config> {

    private final JwtTokenProvider jwtTokenProvider;

    public ManagerAuthorizationHeaderFilter(JwtTokenProvider jwtTokenProvider) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            HttpHeaders headers = request.getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            //JWT 토큰 판별
            String accessToken = headers.get(HttpHeaders.AUTHORIZATION).get(0).replace("Bearer ", "");

            jwtTokenProvider.validateJwtToken(accessToken);

            String memberId = jwtTokenProvider.getUserId(accessToken);

            List<String> roles = jwtTokenProvider.getRoles(accessToken);
            for (String role : roles) {
                System.out.println("role = " + role);
            }

            //권한 check
            if (!jwtTokenProvider.getRoles(accessToken).contains("ROLE_MANAGER")) {
                return onError(exchange, "매니저 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest newRequest = request.mutate()
                    .header("member-id", memberId)
                    .build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }

    //Mono(단일 값), Flux(다중 값) -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        log.error(errorMessage);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }
}
