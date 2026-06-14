package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.dto.ChatRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * AI 助手控制器 — HTTP 网关
 *
 * 把前端 AI 请求转发到 Python 微服务 (python_ai_service:8001).
 * 注入 X-User-Id header 把 JWT 解析后的 user_id 透传给 Python 用于多用户隔离.
 */
@RestController
@RequestMapping("/api/ai")
@Tag(name = "3. AI 助手")
public class AiController {

    private static final ParameterizedTypeReference<ServerSentEvent<String>> SSE_TYPE =
            new ParameterizedTypeReference<>() {};

    private final WebClient pythonClient;

    public AiController(@Value("${python-ai.base-url}") String pythonBaseUrl) {
        this.pythonClient = WebClient.builder()
                .baseUrl(pythonBaseUrl)
                .build();
    }

    @PostMapping("/chat-stream")
    @Operation(summary = "AI 流式对话", description = "转发到 Python 微服务 (SSE 事件: token / node / done)")
    public SseEmitter chatStream(
            @RequestBody ChatRequest req,
            @AuthenticationPrincipal Long userId
    ) {
        // SseEmitter: Spring MVC 原生 SSE 支持, 默认 UTF-8, 不会双重包装
        // timeout 5 分钟 (LLM 长对话也够)
        SseEmitter emitter = new SseEmitter(5 * 60_000L);

        // 用 WebClient 拉 Python 端的 SSE 流 (结构化解析成 ServerSentEvent)
        pythonClient.post()
                .uri("/chat/stream")
                .header("X-User-Id", String.valueOf(userId))   // ⭐ 注入 header
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToFlux(SSE_TYPE)
                .subscribe(
                        sse -> {
                            // 每个 SSE 事件透传给客户端 (用 SseEmitter 重新发送, 保持 event 类型和 data)
                            try {
                                String eventName = sse.event() != null ? sse.event() : "message";
                                String data = sse.data() != null ? sse.data() : "";
                                emitter.send(SseEmitter.event().name(eventName).data(data));
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        emitter::completeWithError,    // 错误回调
                        emitter::complete              // 完成回调
                );

        return emitter;
    }
}
