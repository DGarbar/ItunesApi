package com.dharbar.template.service.musicresources.itunes.devapi.token.generator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class SimpleTokenGenerator {

    private static final String KEY_ALGORITHM = "EC";
    private final KeyFactory keyFactory;

    @Value("${itunes.token.admin.private-key}")
    private String privateKeyFull;

    @Value("${itunes.token.admin.api-key-id}")
    private String apiKeyId;

    @Value("${itunes.token.admin.issuer-id}")
    private String issuerId;

    public SimpleTokenGenerator() throws NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    }

    //String privateKeyFull, String apiKeyId, String issuerId
    public Optional<String> getToken() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(formatPrivateKey(privateKeyFull));
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        return Optional.of(Jwts.builder()
                .setIssuer(issuerId)
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(16))))
                .setAudience("appstoreconnect-v1")
                .setHeaderParam("kid", apiKeyId)
                .setSubject(apiKeyId)
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact());
        } catch (InvalidKeySpecException e) {
            return Optional.empty();
        }
    }

    private String formatPrivateKey(String privateKeyFull) {
        return privateKeyFull.replaceFirst("-----BEGIN PRIVATE KEY-----", "")
                .replaceFirst("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    }
}
