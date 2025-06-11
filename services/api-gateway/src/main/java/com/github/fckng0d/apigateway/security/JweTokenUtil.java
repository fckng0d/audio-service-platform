package com.github.fckng0d.apigateway.security;

import com.github.fckng0d.apigateway.dto.auth.JwtClaimsResponseDto;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.*;

@Component
public class JweTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    public JWTClaimsSet decrypt(String token) throws ParseException, JOSEException {
        JWEObject jwe = JWEObject.parse(token);
        jwe.decrypt(new DirectDecrypter(secret.getBytes()));
        return JWTClaimsSet.parse(jwe.getPayload().toJSONObject());
    }

    public JwtClaimsResponseDto getClaimsByToken(String token) {
        try {
            var claimSet = this.decrypt(token);

            String userId = claimSet.getSubject();
            List<String> roles = Collections.emptyList();

            Object rolesObj = claimSet.getClaim("user_roles");
            if (rolesObj instanceof List<?>) {
                roles = ((List<?>) rolesObj).stream()
                        .map(Object::toString)
                        .toList();
            }

            var expiresAt = claimSet.getExpirationTime().toInstant();

            return JwtClaimsResponseDto.builder()
                    .userId(userId)
                    .roles(roles)
                    .expiresAt(expiresAt)
                    .build();
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
