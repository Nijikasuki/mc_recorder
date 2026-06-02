package com.dy.mcrecorder.mc_recorder.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TavilySearchRequest(
        String query,
        Integer maxResults,
        Boolean includeAnswer
) {}
