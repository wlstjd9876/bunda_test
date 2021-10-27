package kr.bunda.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.bunda.core.model.ErrorResponseModel;
import kr.bunda.core.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Component
@Slf4j
public class AuthenticationFilter implements GatewayFilter {


    private final String jwtsecret;

    private String[] authExcludeUrl;

    private final ObjectMapper om;

    public AuthenticationFilter(@Value("${bunda.token.access.secret}") String secret, @Value("${spring.cloud.gateway.auth-exclude-url}") String url) {
        this.jwtsecret = secret;
        this.om = new ObjectMapper();
        if (url != null) {
            authExcludeUrl = url.split(",");
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        Predicate<ServerHttpRequest> isApiSecured = r -> Arrays.stream(authExcludeUrl)
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        //인증 제외 url 체크
        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                return unauthorizedHandle(exchange, "not found Authorization in header");
            }

            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);
            if (!token.startsWith("Bearer ")) {
                return unauthorizedHandle(exchange, "Invalid Token");
            }
            try {
                Map<String, ?> claims = JwtUtil.getClaims(jwtsecret, token.substring(7));
                if (claims != null) {
                    exchange.getRequest().mutate().header("id", (String) claims.get("id")).build();
                    exchange.getRequest().mutate().header("role", (String) claims.get("role")).build();
                } else {
                    return unauthorizedHandle(exchange, "Expired Token");
                }
            } catch (Exception e) {
                //401 응답
                return unauthorizedHandle(exchange, "Invalid Token");
            }
        }
        exchange.getRequest().mutate().header("ipAddress", Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress()).build();
        return chain.filter(exchange);
    }

    private Mono<Void> unauthorizedHandle(ServerWebExchange exchange, String detail) {
        //401 응답
        ErrorResponseModel er = ErrorResponseModel.builder()
                .type("/error/auth")
                .status(HttpStatus.UNAUTHORIZED.value())
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .detail(detail)
                .instance(exchange.getRequest().getPath().value())
                .build();
        byte[] bytes;
        try {
            bytes = om.writeValueAsBytes(er);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("failed to serialize ErrorResponse");
        }
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}