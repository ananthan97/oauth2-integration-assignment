package com.ois.oauthintegrationservice.core.token;

import java.time.Instant;

public class Token {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final String scope;
    private final Instant expiresAt;

    public Token(
            String accessToken,
            String refreshToken,
            String tokenType,
            String scope,
            Instant expiresAt
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.scope = scope;
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }


    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

}
