package com.ois.oauthintegrationservice.api;

import com.ois.oauthintegrationservice.core.oauth.OAuthClient;
import com.ois.oauthintegrationservice.core.token.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    private final OAuthClient oAuthClient;

    public OAuthController(OAuthClient oAuthClient) {
        this.oAuthClient = oAuthClient;
    }

    @GetMapping("/authorize/{providerId}")
    public ResponseEntity<Void> authorize(@PathVariable String providerId) {
        String state = UUID.randomUUID().toString();
        String authorizationUrl = oAuthClient.getAuthorizationUrl(providerId, state);

        return ResponseEntity
                .status(302)
                .location(URI.create(authorizationUrl))
                .build();
    }

    @GetMapping("callback/{providerId}")
    public ResponseEntity<String> callback(
            @PathVariable String providerId,
            @RequestParam("code") String code
            ) {
        oAuthClient.handleAuthorizationCallback(providerId, code);
        return ResponseEntity.ok("Authorization successful for provider: " + providerId);
    }

    @GetMapping("/token/{providerId}")
    public ResponseEntity<Token> getToken(@PathVariable String providerId) {
        Token token = oAuthClient.getValidToken(providerId);
        return ResponseEntity.ok(token); // NOTE: Exposed only for demo/testing purposes
    }
}
