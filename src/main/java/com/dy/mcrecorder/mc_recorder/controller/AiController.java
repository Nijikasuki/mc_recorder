package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI 助手控制器 — Phase 5 重构占位版
 *
 * 状态: Java Spring AI 已移除, 等 Python 微服务 (LangGraph) 上线后,
 *       此 Controller 改成 HTTP 转发到 python_ai_service 服务
 *
 * 当前所有端点返回 "服务建设中" 占位响应, 前端 AI 助手页可正常加载但无法对话
 */
@RestController
@RequestMapping("/api/ai")
@Tag(name = "3. AI 助手 (Python 重构中)")
public class AiController {

    @PostMapping("/chat")
    @Operation(summary = "AI 对话 (待 Python 服务)", description = "Phase 5 Python LangGraph 重构中")
    public Result<String> chat(@RequestParam String message) {
        return Result.fail(503, "AI 服务正在 Python LangGraph 重构中, 敬请期待");
    }

    @PostMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "AI 流式对话 (待 Python 服务)", description = "Phase 5 Python LangGraph 重构中")
    public Flux<String> chatStream(@RequestParam String message) {
        return Flux.just("AI 服务正在 Python LangGraph 重构中, 敬请期待 🚧");
    }
}
