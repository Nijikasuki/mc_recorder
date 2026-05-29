package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.config.TextOnlyChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final ResonatorTools resonatorTools;
    private final ChatMemory chatMemory;

    public AiService(ChatClient.Builder builder,
                     ResonatorTools resonatorTools,
                     JdbcChatMemoryRepository repository) {
        ChatMemoryRepository filteredRepo = new TextOnlyChatMemoryRepository(repository);
        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(filteredRepo)
                .maxMessages(20)
                .build();
        this.resonatorTools = resonatorTools;
        this.chatClient = builder.defaultAdvisors(
                MessageChatMemoryAdvisor.builder(this.chatMemory).build()
        ).build();
    }

    public String chat(String message,Long userId) {
        return chatClient.prompt(message)
                .tools(spec -> spec
                        .instances(resonatorTools)
                        .context("userId", userId))
                .advisors(a->a.param(
                        ChatMemory.CONVERSATION_ID,
                        "user-" + userId))
                .call()
                .content();
    }
}
