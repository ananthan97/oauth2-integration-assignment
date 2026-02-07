package com.ois.oauthintegrationservice.infra.http.provider;

import com.ois.oauthintegrationservice.core.oauth.OAuthProvider;
import com.ois.oauthintegrationservice.core.token.Token;
import com.ois.oauthintegrationservice.infra.config.OAuthProvidersProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Map;

@Component
public class DefaultOAuthProvider implements OAuthProvider {

    private final OAuthProvidersProperties props;
    private final WebClient webClient;

    public DefaultOAuthProvider(OAuthProvidersProperties props,
                                WebClient webClient
    ) {
        this.props = props;
        this.webClient = webClient;
    }

    @Override
    public boolean supports(String providerId) {
        return props.getProviders().containsKey(providerId);
    }

    @Override
    public String buildAuthorizationUrl(String providerId, String state) {
        var p = props.getProviders().get(providerId);
        return UriComponentsBuilder
                .fromUriString(p.getAuthUrl())
                .queryParam("response_type", "code")
                .queryParam("client_id", p.getClientId())
                .queryParam("redirect_uri", p.getRedirectUri())
                .queryParam("scope", p.getScope())
                .queryParam("state", state)
                .encode()
                .toUriString();
//        return p.getAuthUrl() +
//                "?response_type=code" +
//                "&client_id=" + p.getClientId() +
//                "&redirect_uri=" + p.getRedirectUri() +
//                "&scope=" + p.getScope() +
//                "&state=" + state;
    }

    @Override
    public Token exchangeAuthorizationCode(String providerId, String authorizationCode) {
        var p = props.getProviders().get(providerId);
        Map<String, Object> response =
            webClient.post()
                    .uri(p.getTokenUrl())
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("code", authorizationCode)
                            .with("redirect_uri", p.getRedirectUri())
                            .with("client_id", p.getClientId())
                            .with("client_secret", p.getClientSecret()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        if (response == null) {
            throw new IllegalStateException("OAuth token response is null");
        }
            return mapToToken(response);
    }

    @Override
    public Token refreshAccessToken(String providerId, String refreshToken) {
        var p = props.getProviders().get(providerId);
        Map<String, Object> response =
            webClient.post()
                    .uri(p.getTokenUrl())
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                            .with("refresh_token", refreshToken)
                            .with("client_id", p.getClientId())
                            .with("client_secret", p.getClientSecret()))
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
        Object expires = response.get("expires_in");
        Long expiresIn = expires != null ? Long.parseLong(expires.toString()) : null;


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
