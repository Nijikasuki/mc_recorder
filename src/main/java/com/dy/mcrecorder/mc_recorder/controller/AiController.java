package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.service.AiService;
import com.dy.mcrecorder.mc_recorder.service.InMemoryVectorStore;
import com.dy.mcrecorder.mc_recorder.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "3.AI助手")
public class AiController {
    private final AiService aiService;
    private final EmbeddingModel embeddingModel;
    private final InMemoryVectorStore vectorStore;
    private final RagService ragService;
    public AiController(AiService aiService, EmbeddingModel embeddingModel, InMemoryVectorStore vectorStore,RagService ragService) {
        this.aiService = aiService;
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
        this.ragService = ragService;
    }

    @PostMapping("/chat")
    @Operation(summary = "和 AI 对话", description = "发一条消息给 LLM，同步等返回")
    public Result<String> chat(@RequestParam String message,@AuthenticationPrincipal Long userId) {
        return Result.success(aiService.chat(message,userId));
    }

    @GetMapping("/embed-test")
    public Result<Map<String,Object>> embedTest(@RequestParam String text) {
        float[] vector = embeddingModel.embed(text);
        return Result.success(Map.of(
                "dimension", vector.length,
                "first10", Arrays.copyOf(vector, 10)
        ));
    }

    @GetMapping("/store-size")
    public Result<Integer> storeSize() {
        return Result.success(vectorStore.size());
    }

    @PostMapping("/rag-chat")
    @Operation(summary = "RAG 知识库问答", description = "基于鸣潮百科文档检索 + LLM 回答")
    public Result<String> ragChat(@RequestParam String message,@RequestParam(defaultValue = "3") int topk) {
        return Result.success(ragService.ragChat(message,topk));
    }
}
