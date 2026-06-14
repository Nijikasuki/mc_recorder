package com.dy.mcrecorder.mc_recorder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatRequest {
    private String message;

    @JsonProperty("conversation_id")     // 跟 Python snake_case 对齐
    private String conversationId;

    @JsonProperty("enable_search")
    private boolean enableSearch;

    @JsonProperty("enable_knowledge")
    private boolean enableKnowledge;
}