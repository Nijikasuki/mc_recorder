package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.config.GitHubProperties;
import com.dy.mcrecorder.mc_recorder.dto.GitHubRepo;
import com.dy.mcrecorder.mc_recorder.dto.GitHubRepoActivity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GitHubService {
    private final RestClient restClient;
    public GitHubService(GitHubProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer " + properties.getToken())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    /**
     * 查一个仓库的信息.
     * @param owner 仓库所有者, 如 "spring-projects"
     * @param repo  仓库名, 如 "spring-ai"
     * @return 仓库信息 DTO
     */
    public GitHubRepo getRepo(String owner, String repo) {
        return restClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .body(GitHubRepo.class);
    }

    public List<GitHubRepoActivity> getRepoActivities(String owner, String repo) {
        return restClient.get()
                .uri("/repos/{owner}/{repo}/activity", owner, repo)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GitHubRepoActivity>>() {});
    }
}
