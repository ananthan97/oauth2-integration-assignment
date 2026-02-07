package com.ois.oauthintegrationservice.infra.http.client;

import com.ois.oauthintegrationservice.core.apiclient.SecuredApiClient;
import com.ois.oauthintegrationservice.core.token.Token;
import com.ois.oauthintegrationservice.infra.config.OAuthProvidersProperties;
import org.springframework.web.reactive.function.client.WebClient;

public class DefaultSecuredApiClient implements SecuredApiClient {
    private final WebClient webClient;
    private final OAuthProvidersProperties props;
    public DefaultSecuredApiClient(WebClient webClient, OAuthProvidersProperties props) {
        this.webClient = webClient;
        this.props = props;
    }

    @Override
    public String callApi(String providerId, Token token) {
        var provider = props.getProviders().get(providerId);

        String apiUrl = provider.getApiBaseUrl();
        return webClient.get()
                .uri(apiUrl)
                .headers(h -> h.setBearerAuth(token.getAccessToken()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
