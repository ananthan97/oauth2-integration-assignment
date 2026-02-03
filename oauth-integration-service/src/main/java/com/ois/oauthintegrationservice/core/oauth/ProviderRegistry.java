package com.ois.oauthintegrationservice.core.oauth;

import java.util.List;
import java.util.Map;

public class ProviderRegistry {
    private final List<OAuthProvider> providers;

    public ProviderRegistry(List<OAuthProvider> providers) {
        this.providers = providers;
    }

    public OAuthProvider getProvider(String providerId) {
        return providers.stream()
                .filter(p -> p.supports(providerId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No provider with id " + providerId + " was found.")
                        );
    }
}
