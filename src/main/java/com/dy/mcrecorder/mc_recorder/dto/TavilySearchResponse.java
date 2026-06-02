package com.dy.mcrecorder.mc_recorder.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TavilySearchResponse(
        // ===== 必填字段 =====
        String query,
        String answer,
        List<Image> images,
        List<Result> results,
        Double responseTime,

        // ===== 可选字段(响应里可能没有, 字段为 null) =====
        Map<String, Object> autoParameters,
        Map<String, Object> usage,
        String requestId
) {

    // images 数组的每个元素
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Image(
            String url,
            String description
    ) {}

    // results 数组的每个元素
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Result(
            String title,
            String url,
            String content,
            Double score,
            String rawContent       // 仅 include_raw_content=true 时有, 否则 null
    ) {}
}