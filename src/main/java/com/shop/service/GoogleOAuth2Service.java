package com.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleOAuth2Service extends OAuth2ServiceType {

    @Value("${oauth.google.client.id}")
    private String clientId;

    @Value("${oauth.google.client.secret}")
    private String clientSecret;

    @Value("${oauth.google.url.authorize}")
    private String authorizeUrl;

    @Value("${oauth.google.url.access_token}")
    private String accessTokenUrl;

    @Value("${oauth.google.url.user_info}")
    private String userInfoUrl;

    @Value("${oauth.google.url.redirect}")
    private String redirectUrl;

    @Override
    public String getRedirectURL() {
        return this.getRedirectURL(null);
    }

    public String getRedirectURL(String state) {
        String encodeRedirectUrl = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);

        StringBuilder stringBuilder = new StringBuilder(authorizeUrl);
        stringBuilder.append("?response_type=code");
        stringBuilder.append("&access_type=online");
        stringBuilder.append("&scope=openid profile email");
        stringBuilder.append("&client_id=");
        stringBuilder.append(clientId);
        stringBuilder.append("&redirect_uri=");
        stringBuilder.append(encodeRedirectUrl);

        if(state != null) {
            stringBuilder.append("&state=");
            stringBuilder.append(state);
        }

        return stringBuilder.toString();
    }

    @Override
    public String getToken(String authorizationCode) throws Exception {
        Map<String, Object> parameter = new HashMap<>();

        parameter.put("grant_type", "authorization_code");
        parameter.put("client_id", clientId);
        parameter.put("client_secret", clientSecret);
        parameter.put("redirect_uri", redirectUrl);
        parameter.put("code", authorizationCode);

        Map<String, Object> response = this.sendRequest("POST", accessTokenUrl, parameter);

        return (String) response.get("access_token");
    }

    @Override
    public Map<String, String> getUserInfo(String accessToken) throws Exception {
        Map<String, String> requestHeaders = new HashMap<>();

        requestHeaders.put("Authorization", "Bearer " + accessToken);

        Map<String, Object> response = this.sendRequest("GET", userInfoUrl, null, requestHeaders);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", (String) response.get("email"));
        userInfo.put("name", (String) response.get("name"));

        return userInfo;
    }

}
