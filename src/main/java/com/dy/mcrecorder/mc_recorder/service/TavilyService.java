package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.config.TavilyProperties;
import com.dy.mcrecorder.mc_recorder.dto.TavilySearchRequest;
import com.dy.mcrecorder.mc_recorder.dto.TavilySearchResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class TavilyService {
    private final RestClient restClient;
    public TavilyService(TavilyProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer " + properties.getApiKey())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public TavilySearchResponse search(String query) {
        TavilySearchRequest req = new TavilySearchRequest(query,5,true);
        return restClient.post()
                .uri("/search")
                .body(req)
                .retrieve()
                .body(TavilySearchResponse.class);
    }
}
