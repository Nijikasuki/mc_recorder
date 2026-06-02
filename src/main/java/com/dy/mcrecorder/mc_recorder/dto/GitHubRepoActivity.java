package com.dy.mcrecorder.mc_recorder.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitHubRepoActivity(
        Long id,
        String before,
        String after,
        String ref,
        String timestamp,
        String activityType,            // ← @JsonNaming 自动转 activity_type
        Actor actor
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Actor(
            String login,
            Long id,
            String avatarUrl,            // ← 自动转 avatar_url
            String htmlUrl               // ← 自动转 html_url
    ) {}
}