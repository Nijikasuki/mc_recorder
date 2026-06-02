package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.entity.Resonator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ResonatorTools {
    private final ResonatorService resonatorService;
    public ResonatorTools(ResonatorService resonatorService) {
        this.resonatorService = resonatorService;
    }

    @Tool(description = "获取当前登录用户拥有的所有鸣潮角色（共鸣者）列表。" +
            "在需要给用户做角色养成建议、推荐培养优先级、推荐配队等场景时调用此工具。" +
            "返回每个角色的 id、名字、属性、星级、等级、共鸣链等级。")
    public List<Resonator> getMyResonators(ToolContext toolContext) {
        // ToolContext 是 Spring AI 提供的"隐藏参数"，LLM 看不到它
        // 我们从中取 Controller 传进来的 userId
        Long userId = (Long) toolContext.getContext().get("userId");
        log.info("[Tool] getMyResonators 被 LLM 调用了, userId={}" , userId);
        return resonatorService.findAll(userId);
    }
}
