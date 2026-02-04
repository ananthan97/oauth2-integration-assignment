# oauth2-integration-assignment
Assignment provided by Revsure AI to evaluate the understanding of OAuth concepts and Backend Integration.

# OAuth Integration Service
A small, extensible OAuth2 integration framework built using Spring Boot that supports the Authorization Code flow, token management, and secured API calls.
The design is provider-agnostic and can support multiple OAuth providers with minimal configuration changes.


# Key Design Principles
* Provider-agnostic: No provider-specific logic in core layers
* Configuration-driven: Providers added via application.properties
* Extensible: Supports future providers like Google, Azure, Okta


# Setup Instructions
## Prerequisites
  * Java 17+
  * Maven 3.8+
  * GitHub account

## Register OAuth App (GitHub)
  * Go to: GitHub → Settings → Developer settings → OAuth Apps
  * Create a new OAuth App
  * ### Set Authorization callback URL   
http://localhost:8080/oauth/callback/github
  * ### Environment Variables  
Set the following environment variables:  
    OAUTH_GITHUB_CLIENT_ID=your_client_id  
    OAUTH_GITHUB_CLIENT_SECRET=your_client_secret
  * ### application.properties  
    spring.application.name=oauth-integration-service
    server.port=8080  
    oauth.providers.github.provider-id=github  
    oauth.providers.github.auth-url=https://github.com/login/oauth/authorize  
    oauth.providers.github.token-url=https://github.com/login/oauth/access_token  
    oauth.providers.github.api-base-url=https://api.github.com  
    oauth.providers.github.client-id=\${OAUTH_GITHUB_CLIENT_ID}  
    oauth.providers.github.client-secret=\${OAUTH_GITHUB_CLIENT_SECRET}  
    oauth.providers.github.redirect-uri=http://localhost:8080/oauth/callback/github  
    oauth.providers.github.scope=read:user
  * ### Run the Application  
    mvn clean spring-boot:run

## Testing Endpoints (Browser)
* List configured providers - http://localhost:8080/oauth/providers
* Start OAuth flow - http://localhost:8080/oauth/authorize/github
* View stored token (demo only) - http://localhost:8080/oauth/token/github
* Call secured GitHub API - http://localhost:8080/oauth/me/github

# OAuth flow
<img width="1536" height="1024" alt="Architecture overview" src="https://github.com/user-attachments/assets/d6967380-16f0-4b35-a72d-27af1e56387e" />

# Folder Structure
<img width="443" height="576" alt="image" src="https://github.com/user-attachments/assets/caab1101-d1ad-4f28-965e-f398a6785eb8" />
<img width="411" height="426" alt="image" src="https://github.com/user-attachments/assets/b125d459-6eba-4415-8f2b-a351b1afff6b" />

# Architecture Overview
This service is a provider-agnostic OAuth2 integration framework built using Spring Boot.
It cleanly separates HTTP APIs, OAuth orchestration, and provider-specific implementations to allow easy onboarding of new OAuth providers.

## Layered Design
### API Layer - Handles HTTP requests and responses
* Exposes OAuth endpoints:
  * /oauth/authorize/{providerId}
  * /oauth/callback/{providerId}
  * /oauth/me/{providerId}
* No OAuth logic inside controllers
### Core Layer - Framework-agnostic domain logic
* OAuthClient  
Orchestrates the OAuth flow (authorize, callback, refresh, token validation)
* OAuthProvider (interface)  
Contract for OAuth providers
* ProviderRegistry  
Resolves providers dynamically using providerId
* TokenStore (interface)  
Abstracts token persistence
* SecuredApiClient (interface)  
Contract for calling secured APIs
### Infrastructure Layer - Concrete implementations
* DefaultOAuthProvider  
Implements OAuth2 Authorization Code flow using WebClient
* InMemoryTokenStore  
Stores tokens per provider (demo-friendly, replaceable)
* DefaultSecuredApiClient  
Calls provider APIs using Bearer tokens
* Configuration-driven provider metadata  
(application.properties)

# Provider-Agnostic Design
OAuth providers are defined purely through configuration.
To add a new provider:

* Add provider config in application.properties

* Restart the application

* Use /oauth/authorize/{providerId}

No code changes required.

