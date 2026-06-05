package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "3.AI助手")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    @Operation(summary = "和 AI 对话", description = "发一条消息给 LLM，同步等返回")
    public Result<String> chat(@RequestParam String message,@AuthenticationPrincipal Long userId) {
        return Result.success(aiService.chat(message,userId));
    }

    @PostMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "和 AI 流式对话", description = "返回 Server-Sent Events 流, 字逐个蹦")
    public Flux<String> chatStream(@RequestParam String message, @AuthenticationPrincipal Long userId) {
        return aiService.chatStream(message, userId);
    }
}
