//package com.github.fckng0d.apigateway.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Component
//@Order(2)
//public class RoleCheckGlobalFilter implements GlobalFilter {
//
//    private static final String USER_ROLES_ATTR = "user_roles";
//    private static final String ROLE_POSTFIX = "_ROLE";
//
//    private static final Map<String, List<String>> ROUTE_ROLES = Map.of(
//            "/user/[0-9a-fA-F-]{36})(/.*)?", List.of("LISTENER", "ADMIN")
//    );
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getPath().toString();
//
//        // Получаем роли для пути
//        Optional<List<String>> requiredRoles = ROUTE_ROLES.entrySet().stream()
//                .filter(entry ->
//                        path.matches(entry.getKey()) || path.startsWith(entry.getKey()))
//                .map(Map.Entry::getValue)
//                .findFirst();
//
//        // Если нет совпадений ролей - запрещаем доступ
//        List<String> userRoles = exchange.getAttributeOrDefault(USER_ROLES_ATTR, List.of());
//        if (requiredRoles.isPresent() && !hasAccess(requiredRoles.get(), userRoles)) {
//            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//            return exchange.getResponse().setComplete();
//        }
//
//        return chain.filter(exchange);
//    }
//
//    private boolean hasAccess(List<String> requiredRoles, List<String> userRoles) {
//        return requiredRoles.stream()
//                .map(role -> role.concat(ROLE_POSTFIX))
//                .anyMatch(userRoles::contains);
//    }
//
//}
//
