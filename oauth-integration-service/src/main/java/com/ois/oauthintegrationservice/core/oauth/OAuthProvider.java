package com.ois.oauthintegrationservice.core.oauth;

import com.ois.oauthintegrationservice.core.token.Token;

public interface OAuthProvider {

    String getProviderId();
    String buildAuthorizationUrl(String state);
    Token exchangeAuthorizationCode(String authorizationCode);
    Token refreshAccessToken(String refreshToken);
}
