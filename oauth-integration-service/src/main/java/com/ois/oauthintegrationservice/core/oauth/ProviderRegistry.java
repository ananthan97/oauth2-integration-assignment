package com.ois.oauthintegrationservice.core.oauth;

import com.ois.oauthintegrationservice.infra.config.OAuthProvidersProperties;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProviderRegistry {
    private final OAuthProvidersProperties properties;
    private final List<OAuthProvider> providers;

    public ProviderRegistry(
            OAuthProvidersProperties properties,
            List<OAuthProvider> providers
    ) {
        this.properties = properties;
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

    public Set<String> getProviderIds() {
        return properties.getProviders().keySet();
    }



}
