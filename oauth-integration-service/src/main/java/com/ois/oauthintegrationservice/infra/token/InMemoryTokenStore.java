package com.ois.oauthintegrationservice.infra.token;

import com.ois.oauthintegrationservice.core.token.Token;
import com.ois.oauthintegrationservice.core.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryTokenStore implements TokenStore {

    private final ConcurrentMap<String, Token> store = new ConcurrentHashMap<>();

    @Override
    public void save(String providerId, Token token){
        store.put(providerId, token);
    }

    @Override
    public Optional<Token> get(String providerId) {
        return Optional.ofNullable(store.get(providerId));
    }

    @Override
    public void remove(String providerId){
        store.remove(providerId);
    }
}
