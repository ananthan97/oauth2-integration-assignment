package com.ois.oauthintegrationservice;

import com.ois.oauthintegrationservice.infra.config.OAuthProvidersProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class OauthIntegrationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthIntegrationServiceApplication.class, args);
    }

}
