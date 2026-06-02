package com.dy.mcrecorder.mc_recorder.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitHubRepo(
        Long id,
        String name,
        String fullName,
        String description,
        Integer stargazersCount,
        Integer forksCount,
        Integer openIssuesCount,
        String language,
        String defaultBranch,
        String htmlUrl
) {}
