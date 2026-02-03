package com.ois.oauthintegrationservice.infra.config;

import com.ois.oauthintegrationservice.core.oauth.OAuthClient;
import com.ois.oauthintegrationservice.core.oauth.OAuthProvider;
import com.ois.oauthintegrationservice.core.oauth.ProviderRegistry;
import com.ois.oauthintegrationservice.core.token.TokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class OAuthWiringConfig {
    @Bean
    public ProviderRegistry providerRegistry(List<OAuthProvider> providers) {
        return new ProviderRegistry(providers);
    }

    @Bean
    public OAuthClient oAuthClient(
            ProviderRegistry providerRegistry,
            TokenStore tokenStore
    ) {
        return new OAuthClient(providerRegistry, tokenStore);
    }
}
