package com.github.fckng0d.authenticationservice.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class JweTokenUtil {
    private static final String USER_ROLES_ATTR = "user_roles";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration-ms}")
    private long accessExpiration;

    public String generateEncryptedToken(UUID userId, Set<String> roles) throws JOSEException {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .claim(USER_ROLES_ATTR, roles)
                .expirationTime(Date.from(Instant.now().plusMillis(accessExpiration)))
                .build();

        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256CBC_HS512);
        Payload payload = new Payload(claims.toJSONObject());
        JWEObject jwe = new JWEObject(header, payload);

        jwe.encrypt(new DirectEncrypter(secret.getBytes()));
        return jwe.serialize();
    }
}
