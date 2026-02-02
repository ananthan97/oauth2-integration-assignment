package com.ois.oauthintegrationservice.core.oauth;

import java.util.Map;

public class ProviderRegistry {
    private final Map<String, OAuthProvider> providers;

    public ProviderRegistry(Map<String, OAuthProvider> providers) {
        this.providers = providers;
    }

    public OAuthProvider getProvider(String providerId) {
        OAuthProvider provider = providers.get(providerId);
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported OAuth Provider Id: " + providerId);
        }
        return provider;
    }
}
