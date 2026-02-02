package com.ois.oauthintegrationservice.core.token;
import java.util.Optional;

public interface TokenStore {
    void save(String providerId, Token token);
    Optional<Token> get(String providerId);
    void remove(String providerId);
}
