package com.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverOAuth2Service extends OAuth2ServiceType {

    @Value("${oauth.naver.client.id}")
    private String clientId;

    @Value("${oauth.naver.client.secret}")
    private String clientSecret;

    @Value("${oauth.naver.url.authorize}")
    private String authorizeUrl;

    @Value("${oauth.naver.url.access_token}")
    private String accessTokenUrl;

    @Value("${oauth.naver.url.user_info}")
    private String userInfoUrl;

    @Value("${oauth.naver.url.redirect}")
    private String redirectUrl;

    @Override
    public String getRedirectURL() {
        SecureRandom random = new SecureRandom();

        String stateCode = new BigInteger(130, random).toString();

        String encodeRedirectUrl = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);

        StringBuilder stringBuilder = new StringBuilder(authorizeUrl);
        stringBuilder.append("?response_type=code");
        stringBuilder.append("&client_id=");
        stringBuilder.append(clientId);
        stringBuilder.append("&redirect_uri=");
        stringBuilder.append(encodeRedirectUrl);
        stringBuilder.append("&state=");
        stringBuilder.append(stateCode);

        return stringBuilder.toString();
    }

    @Override
    public String getToken(String authorizationCode) throws Exception {
        return this.getToken(null, authorizationCode);
    }

    public String getToken(String stateCode, String authorizationCode) throws Exception {
        String encodeRedirectUrl = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);

        StringBuilder stringBuilder = new StringBuilder(accessTokenUrl);
        stringBuilder.append("?grant_type=authorization_code");
        stringBuilder.append("&client_id=");
        stringBuilder.append(clientId);
        stringBuilder.append("&client_secret=");
        stringBuilder.append(clientSecret);
        stringBuilder.append("&redirect_uri=");
        stringBuilder.append(encodeRedirectUrl);
        stringBuilder.append("&code=");
        stringBuilder.append(authorizationCode);
        stringBuilder.append("&state=");
        stringBuilder.append(stateCode);

        Map<String, Object> response = this.sendRequest("GET", stringBuilder.toString());

        return (String) response.get("access_token");
    }

    @Override
    public Map<String, String> getUserInfo(String accessToken) throws Exception {
        Map<String, String> requestHeaders = new HashMap<>();

        requestHeaders.put("Authorization", "Bearer " + accessToken);

        Map<String, Object> response = this.sendRequest("GET", userInfoUrl, null, requestHeaders);
        Map<String, String> userInfo = (Map<String, String>) response.get("response");

        return userInfo;
    }

}
