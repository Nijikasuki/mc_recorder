package com.dy.mcrecorder.mc_recorder.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class KnowledgeLoader {
    private final VectorStore vectorStore;
    public KnowledgeLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadKnowledge() throws IOException {
        var existing = vectorStore.similaritySearch(
                SearchRequest.builder().query("琳奈").topK(1).build()
        );
        if (!existing.isEmpty()) {
            log.info("知识库已有数据, 跳过加载");
            return;
        }

        ClassPathResource resource = new ClassPathResource("knowledge/wuwa.md");
        String content = resource.getContentAsString(StandardCharsets.UTF_8);

        List<Document> documents = Arrays.stream(content.split("\\R---\\R"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(text -> new Document(text, java.util.Map.of("source", "wuwa.md")))
                .toList();
        vectorStore.add(documents);
        log.info("知识库加载完成, 共 {} 块", documents.size());
    }
}
