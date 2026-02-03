package com.ois.oauthintegrationservice.infra.config;

import com.ois.oauthintegrationservice.core.apiclient.SecuredApiClient;
import com.ois.oauthintegrationservice.infra.http.client.DefaultSecuredApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiClientConfig {

    @Bean
    public SecuredApiClient securedApiClient(
        WebClient webClient,
        OAuthProvidersProperties props
    ){
        return new DefaultSecuredApiClient(webClient, props);
    }
}
