package com.github.fckng0d.apigateway.filter;

import com.github.fckng0d.apigateway.security.JweTokenUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.Config> {

    private final JweTokenUtil jweTokenUtil;

    public JwtValidationGatewayFilterFactory(JweTokenUtil jweTokenUtil) {
        super(Config.class);
        this.jweTokenUtil = jweTokenUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            var authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorized(exchange);
            }

            String token = authHeader.substring(7);
            try {
                JWTClaimsSet claims = jweTokenUtil.decrypt(token);

                Date expiration = claims.getExpirationTime();
                if (expiration == null || expiration.before(new Date())) {
                    return unauthorized(exchange);
                }
                exchange.getAttributes().put("userId", claims.getSubject());

                return chain.filter(exchange);

            } catch (Exception e) {
                return unauthorized(exchange);
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // можно добавить конфигурацию, если нужно
    }
}
