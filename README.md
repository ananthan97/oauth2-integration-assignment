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

# Architecture Overview
<img width="1536" height="1024" alt="Architecture overview" src="https://github.com/user-attachments/assets/d6967380-16f0-4b35-a72d-27af1e56387e" />


