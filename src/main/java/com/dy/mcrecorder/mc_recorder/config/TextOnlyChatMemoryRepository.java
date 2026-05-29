package com.dy.mcrecorder.mc_recorder.config;

import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;

import java.util.List;

/**
 * 装饰器: 包装任意 ChatMemoryRepository,
 * 在 saveAll 时过滤掉 tool 相关消息,避开 Spring AI M7/M8 的
 * "toolCallId not set on rehydrate" bug
 */
public class TextOnlyChatMemoryRepository implements ChatMemoryRepository {
    private final ChatMemoryRepository delegate;
    public TextOnlyChatMemoryRepository(ChatMemoryRepository delegate) {
        this.delegate = delegate;
    }
    @Override
    public List<Message> findByConversationId(String conversationId) {
        return delegate.findByConversationId(conversationId);
    }

    @Override
    public List<String> findConversationIds() {
        return delegate.findConversationIds();
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        delegate.deleteByConversationId(conversationId);
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        List<Message> textOnly = messages.stream()
                .filter(m -> !(m instanceof ToolResponseMessage))
                .filter(m -> !(m instanceof AssistantMessage am && am.hasToolCalls()))
                .toList();
        delegate.saveAll(conversationId, textOnly);
    }
}
