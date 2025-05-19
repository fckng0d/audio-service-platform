package com.github.fckng0d.apigateway.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class JweTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    public JWTClaimsSet decrypt(String token) throws ParseException, JOSEException {
        JWEObject jwe = JWEObject.parse(token);
        jwe.decrypt(new DirectDecrypter(secret.getBytes()));
        return JWTClaimsSet.parse(jwe.getPayload().toJSONObject());
    }
}
