package com.dy.mcrecorder.mc_recorder.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class KnowledgeLoader {
    private final EmbeddingModel embeddingModel;
    private final InMemoryVectorStore vectorStore;
    public KnowledgeLoader(EmbeddingModel embeddingModel, InMemoryVectorStore vectorStore) {
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadKnowledge() throws IOException {
        ClassPathResource resource = new ClassPathResource("knowledge/wuwa.md");
        String content = resource.getContentAsString(StandardCharsets.UTF_8).replace("\r\n", "\n");

        String[] chunks = content.split("\\n---\\n");
        int count = 0;
        for (String chunk : chunks) {
            chunk = chunk.trim();
            if (chunk.isEmpty()) continue;
            vectorStore.add(chunk, embeddingModel.embed(chunk));
            count++;
        }
        log.info("知识库加载完成, 共 {} 块", count);
    }
}
