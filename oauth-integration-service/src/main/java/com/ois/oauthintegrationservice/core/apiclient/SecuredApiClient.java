package com.ois.oauthintegrationservice.core.apiclient;

import com.ois.oauthintegrationservice.core.token.Token;

public interface SecuredApiClient {
    String callApi(String providerId, Token token);
}
