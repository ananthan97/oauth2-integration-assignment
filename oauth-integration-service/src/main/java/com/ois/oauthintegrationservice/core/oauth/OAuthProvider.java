package com.ois.oauthintegrationservice.core.oauth;

import com.ois.oauthintegrationservice.core.token.Token;

public interface OAuthProvider {

    boolean supports(String providerId);
    String buildAuthorizationUrl(String providerId, String state);
    Token exchangeAuthorizationCode(String providerId, String authorizationCode);
    Token refreshAccessToken(String providerId, String refreshToken);
}
