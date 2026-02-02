package com.ois.oauthintegrationservice.core.oauth;

import com.ois.oauthintegrationservice.core.token.Token;
import com.ois.oauthintegrationservice.core.token.TokenStore;

public class OAuthClient {
    private final ProviderRegistry providerRegistry;
    private final TokenStore tokenStore;

    public OAuthClient(
            ProviderRegistry providerRegistry,
            TokenStore tokenStore
    ) {
        this.providerRegistry = providerRegistry;
        this.tokenStore = tokenStore;
    }

    public String getAuthorizationUrl(String providerId, String state) {
        OAuthProvider provider = providerRegistry.getProvider(providerId);
        return provider.buildAuthorizationUrl(state);
    }

    public void handleAuthorizationCallback(
            String providerId,
            String authorizationCode
    ) {
        OAuthProvider provider = providerRegistry.getProvider(providerId);
        Token token = provider.exchangeAuthorizationCode(authorizationCode);
        tokenStore.save(providerId, token);
    }

    public Token getValidToken(String providerId) {
        Token token = tokenStore.get(providerId)
                .orElseThrow(() ->
                        new IllegalStateException("No valid token found for provider " + providerId)
                );
        if(token.isExpired()) {
            OAuthProvider provider = providerRegistry.getProvider(providerId);
            Token refreshed = provider.refreshAccessToken(token.getRefreshToken());
            tokenStore.save(providerId, refreshed);
            return refreshed;
        }
        return token;
    }
}
