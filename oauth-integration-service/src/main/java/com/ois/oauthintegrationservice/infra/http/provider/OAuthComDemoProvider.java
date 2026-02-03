package com.ois.oauthintegrationservice.infra.http.provider;

import com.ois.oauthintegrationservice.core.oauth.OAuthProvider;
import com.ois.oauthintegrationservice.core.token.Token;
import com.ois.oauthintegrationservice.infra.config.OAuthDemoProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;

@Component
public class OAuthComDemoProvider implements OAuthProvider {

    private final OAuthDemoProperties props;
    private final WebClient webClient;

    public OAuthComDemoProvider(OAuthDemoProperties props,
                                WebClient webClient
    ) {
        this.props = props;
        this.webClient = webClient;
    }

    @Override
    public String getProviderId() {
        return props.getProviderId();
    }

    @Override
    public String buildAuthorizationUrl(String state) {
        return props.getAuthURL() +
                "?response_type=code" +
                "&client_id=" + props.getClientId() +
                "&redirect_uri=" + props.getRedirectUri() +
                "&scope=" + props.getScope() +
                "&state=" + state;
    }

    @Override
    public Token exchangeAuthorizationCode(String authorizationCode) {
        Map<String, Object> response =
            webClient.post()
                    .uri(props.getTokenURL())
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("code", authorizationCode)
                            .with("redirect_uri", props.getRedirectUri())
                            .with("client_id", props.getClientId())
                            .with("client_secret", props.getClientSecret()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        if (response == null) {
            throw new IllegalStateException("OAuth token response is null");
        }
            return mapToToken(response);
    }

    @Override
    public Token refreshAccessToken(String refreshToken) {
        Map<String, Object> response =
            webClient.post()
                    .uri(props.getTokenURL())
                    .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                            .with("refresh_token", refreshToken)
                            .with("client_id", props.getClientId())
                            .with("client_secret", props.getClientSecret()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        if (response == null) {
            throw new IllegalStateException("OAuth token response is null");
        }
            return mapToToken(response);
    }

    private Token mapToToken(Map<String, Object> response) {
        String accessToken = (String) response.get("access_token");
        String refreshToken = (String) response.get("refresh_token");
        String tokenType = (String) response.get("token_type");
        String scope = (String) response.get("scope");
        Integer expiresIn = (Integer) response.get("expires_in");

        Instant expiresAt = expiresIn != null
                ? Instant.now().plusSeconds(expiresIn)
                : null;

        return new Token(
                accessToken,
                refreshToken,
                tokenType,
                scope,
                expiresAt
        );
    }

}
