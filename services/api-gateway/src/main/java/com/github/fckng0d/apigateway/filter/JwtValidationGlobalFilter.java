//package com.github.fckng0d.apigateway.filter;
//
//import com.github.fckng0d.apigateway.security.JweTokenUtil;
//import com.nimbusds.jwt.JWTClaimsSet;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@Order(1)
//@RequiredArgsConstructor
//public class JwtValidationGlobalFilter implements GlobalFilter {
//
//    public static final String BEARER_PREFIX = "Bearer ";
//    private static final String USER_ID_ATTR = "user_id";
//    private static final String USER_ROLES_ATTR = "user_roles";
//    private final JweTokenUtil jweTokenUtil;
//
//    // Список публичных путей, не требующих аутентификации
//    private static final List<String> publicPaths = List.of(
//            "/swagger-ui",
//            "/v3/api-docs",
//            "/actuator/health",
//            "/auth",
//            "/user/profile",
//            "/musician/[0-9a-fA-F-]{36}",
//            "/album/[0-9a-fA-F-]{36}",
//            "/playlist/[0-9a-fA-F-]{36}"
//    );
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        if (this.isPublicEndpoint(exchange.getRequest())) {
//            return chain.filter(exchange);
//        }
//
//        var authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
//            return this.unauthorized(exchange);
//        }
//
//        String token = authHeader.substring(7);
//        try {
//            JWTClaimsSet claims = jweTokenUtil.decrypt(token);
//
//            if (this.isTokenExpired(claims.getExpirationTime())) {
//                return this.unauthorized(exchange);
//            }
//
//            this.enrichRequestWithTokenData(exchange, claims);
//
//            return chain.filter(exchange);
//
//        } catch (Exception e) {
//            return this.unauthorized(exchange);
//        }
//    }
//
//    private boolean isPublicEndpoint(ServerHttpRequest httpRequest) {
//        String path = httpRequest.getPath().toString();
//
//        return publicPaths.stream().anyMatch(publicPath ->
//                path.matches(publicPath) ||                // Для regex
//                        path.startsWith(publicPath + "/") ||       // Для префиксов
//                        path.equals(publicPath)                    // Точное совпадение
//        );
//    }
//
//    private boolean isTokenExpired(Date expiration) {
//        return expiration == null || expiration.before(new Date());
//    }
//
//    private void enrichRequestWithTokenData(ServerWebExchange exchange, JWTClaimsSet claims) {
//        exchange.getAttributes().put(USER_ID_ATTR, claims.getSubject());
//
//        Object rolesObj = claims.getClaim(USER_ROLES_ATTR);
//        if (rolesObj instanceof List<?>) {
//            List<String> roles = ((List<?>) rolesObj).stream()
//                    .map(Object::toString)
//                    .toList();
//            exchange.getAttributes().put(USER_ROLES_ATTR, roles);
//        }
//    }
//
//    private Mono<Void> unauthorized(ServerWebExchange exchange) {
//        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        exchange.getResponse().getHeaders().add("WWW-Authenticate", "Bearer");
//        return exchange.getResponse().setComplete();
//    }
//}