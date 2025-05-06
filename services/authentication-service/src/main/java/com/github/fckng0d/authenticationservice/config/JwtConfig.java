//package com.github.fckng0d.authenticationservice.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class JwtConfig {
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.access-token-expiration}")
//    private Long accessTokenExpiration;
//
//    @Value("${jwt.refresh-token-expiration}")
//    private Long refreshTokenExpiration;
//
//    @Bean
//    public JwtEncoder jwtEncoder() {
//        return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(
//                secret.getBytes(),
//                "HmacSHA256"
//        )).build();
//    }
//
//    @Bean
//    public JwtGenerator jwtGenerator() {
//        return new JwtGenerator(this);
//    }
//
//    @Bean
//    public JwtValidator jwtValidator() {
//        return new JwtValidator(this);
//    }
//}