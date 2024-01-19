package com.nat.nexastream.integration.external.event;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>();

        //Agregar informacion extra
        info.put("name-session", oAuth2Authentication.getName());

        DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) oAuth2AccessToken;
        defaultOAuth2AccessToken.setAdditionalInformation(info);
        return defaultOAuth2AccessToken;

    }
}

