package com.recipe.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NaverSearchService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @Value("${naver.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public NaverSearchService() {
        this.restTemplate = new RestTemplate();
    }

    public com.recipe.dto.NaverSearchResponse search(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        String encodedQuery = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("query", query)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .build()
                .toUriString();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                encodedQuery,
                HttpMethod.GET,
                entity,
                com.recipe.dto.NaverSearchResponse.class
        ).getBody();
    }
}
