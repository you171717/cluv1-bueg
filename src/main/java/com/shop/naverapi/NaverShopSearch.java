package com.shop.naverapi;


import com.shop.dto.NaverApiDto;
import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Log
public class NaverShopSearch{

    @Value("${shop.naver.client.id}")
    private String clientId;
    @Value("${shop.naver.client.secret}")
    private String clientSecret;



    public List<NaverApiDto> search(String query) {

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", clientId);
        headers.add("X-Naver-Client-Secret", clientSecret);
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        // 넘겨받은 query로 검색 요청
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/shop.json?query=" + query,
                HttpMethod.GET, requestEntity, String.class);
        String response = responseEntity.getBody();

        return fromJSONtoItems(response);
    }

    public List<NaverApiDto> fromJSONtoItems(String result) {
        JSONObject rjson = new JSONObject(result);
        JSONArray items = rjson.getJSONArray("items");
        List<NaverApiDto> ret = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = items.getJSONObject(i);
            NaverApiDto naverApiDto = new NaverApiDto(itemJson);
            ret.add(naverApiDto);
        }
        return ret;
    }


    public List<NaverApiDto> search2(String query) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", clientId);
        headers.add("X-Naver-Client-Secret", clientSecret);
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        // 넘겨받은 query로 검색 요청
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/shop.json?query=" + query,
                HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        String response = responseEntity.getBody();

        return fromJSONtoItems2(response);
    }

    public List<NaverApiDto> fromJSONtoItems2(String result) {
        JSONObject rjson = new JSONObject(result);
        JSONArray items = rjson.getJSONArray("items");
        List<NaverApiDto> ret = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            JSONObject itemJson = items.getJSONObject(i);
            NaverApiDto naverApiDto = new NaverApiDto(itemJson);
            ret.add(naverApiDto);
        }
        return ret;
    }





}
