package com.dharbar.template.service.musicresources.itunes.devapi.token.generator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class TokenGenerator {

    private static final String KEY_ALGORITHM = "EC";
    private final KeyFactory keyFactory;

    @Value("${itunes.token.admin.private-key}")
    private String privateKeyFull;

    @Value("${itunes.token.admin.api-key-id}")
    private String apiKeyId;

    @Value("${itunes.token.admin.issuer-id}")
    private String issuerId;

    public TokenGenerator() throws NoSuchAlgorithmException {
        keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    }

    //String privateKeyFull, String apiKeyId, String issuerId
    public Optional<String> getToken() {
        return getToken(privateKeyFull, apiKeyId, issuerId);
    }

    public Optional<String> getToken(String privateKeyFull, String apiKeyId, String issuerId) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(formatPrivateKey(privateKeyFull));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return Optional.of(Jwts.builder()
                    .setIssuer(issuerId)
                    .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(16))))
                    .setAudience("appstoreconnect-v1")
                    .setHeaderParam("kid", apiKeyId)
                    .setSubject(apiKeyId)
                    .signWith(privateKey, SignatureAlgorithm.ES256)
                    .compact());
        } catch (InvalidKeySpecException e) {
            log.error("Bad JWT for apiKeyId " + apiKeyId, e);
            return Optional.empty();
        }
    }

    private String formatPrivateKey(String privateKeyFull) {
        return privateKeyFull.replaceFirst("-----BEGIN PRIVATE KEY-----", "")
                .replaceFirst("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "")
                .replaceAll("\\n", " ");
    }
}
