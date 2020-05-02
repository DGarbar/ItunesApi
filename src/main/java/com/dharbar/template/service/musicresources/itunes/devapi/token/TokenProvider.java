package com.dharbar.template.service.musicresources.itunes.devapi.token;

import com.dharbar.template.service.musicresources.itunes.devapi.token.generator.SimpleTokenGenerator;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenProvider {

    private final SimpleTokenGenerator tokenGenerator;
    private final Map<String, String> tokenCache;

    public TokenProvider(SimpleTokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
        tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(20)
                .<String, String>build().asMap();
    }

    public String provideToken() {
        String token = tokenCache.get("ADMIN");
        if (StringUtils.isBlank(token)) {
            String generateToken = generateToken();
            tokenCache.put("ADMIN", generateToken);

            log.warn(generateToken);
            return generateToken;
        }
        return token;
    }

    private String generateToken() {
        return tokenGenerator.getToken()
                .orElseThrow(() -> new IllegalArgumentException("Token was not provided"));
    }


}
