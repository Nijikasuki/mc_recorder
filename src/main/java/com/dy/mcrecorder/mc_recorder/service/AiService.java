package com.dy.mcrecorder.mc_recorder.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final ResonatorTools resonatorTools;

    public AiService(ChatClient.Builder builder,ResonatorTools resonatorTools) {
        this.chatClient = builder.build();
        this.resonatorTools = resonatorTools;
    }

    public String chat(String message,Long userId) {
        return chatClient.prompt(message)
                .tools(spec -> spec
                        .instances(resonatorTools)
                        .context("userId", userId))
                .call()
                .content();
    }
}
