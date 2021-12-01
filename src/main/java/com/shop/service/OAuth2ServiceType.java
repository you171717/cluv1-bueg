package com.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class OAuth2ServiceType {

    public abstract String getRedirectURL();

    public abstract String getToken(String authorizationCode) throws Exception;

    public abstract Map<String, String> getUserInfo(String accessToken) throws Exception;

    protected Map<String, Object> sendRequest(String method, String requestUrl) throws Exception {
        return this.sendRequest(method, requestUrl, null, null, HttpURLConnection.HTTP_OK);
    }

    protected Map<String, Object> sendRequest(String method, String requestUrl, Map<String, Object> parameter) throws Exception {
        return this.sendRequest(method, requestUrl, parameter, null, HttpURLConnection.HTTP_OK);
    }

    protected Map<String, Object> sendRequest(String method, String requestUrl, Map<String, Object> parameter, Map<String, String> requestHeaders) throws Exception {
        return this.sendRequest(method, requestUrl, parameter, requestHeaders, HttpURLConnection.HTTP_OK);
    }

    protected Map<String, Object> sendRequest(String method, String requestUrl, Map<String, Object> parameter, Map<String, String> requestHeaders, int expectResponseCode) throws Exception {
        final boolean isGet = method.equalsIgnoreCase("GET");
        final boolean isPost = method.equalsIgnoreCase("POST");

        StringBuilder queryStringBuilder = new StringBuilder();

        if(parameter != null) {
            for (Map.Entry<String, Object> param : parameter.entrySet()) {
                if (queryStringBuilder.length() != 0)
                    queryStringBuilder.append('&');

                queryStringBuilder.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
                queryStringBuilder.append('=');
                queryStringBuilder.append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8));
            }
        }

        String queryString = queryStringBuilder.toString();

        URL url = new URL(isGet ? requestUrl + queryString : requestUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(method);

        if(isPost) {
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }

        if(requestHeaders != null) {
            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        if(isPost) {
            byte[] queryStringBytes = queryString.getBytes(StandardCharsets.UTF_8);

            conn.setRequestProperty("Content-Length", String.valueOf(queryStringBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(queryStringBytes);
        }

        BufferedReader br;

        int responseCode = conn.getResponseCode();

        if(expectResponseCode != 0 && responseCode == expectResponseCode) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder content = new StringBuilder();
        String line;

        while((line = br.readLine()) != null) {
            content.append(line);
        }

        br.close();

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> json = objectMapper.readValue(content.toString(), new TypeReference<Map<String, Object>>() {});

        if(responseCode == expectResponseCode) {
            return json;
        } else {
            throw new IllegalStateException(json.toString());
        }
    }

}
