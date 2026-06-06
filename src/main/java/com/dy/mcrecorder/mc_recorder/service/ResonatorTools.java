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

    public record ResonatorSummary(int total, List<Resonator> characters) {}

    @Tool(description = "获取当前登录用户拥有的所有鸣潮角色（共鸣者）列表。" +
            "在需要给用户做角色养成建议、推荐培养优先级、推荐配队等场景时调用此工具。" +
            "返回结构: { total: 角色总数, characters: [{id, 名字, 属性, 星级, 等级, 共鸣链等级}] }。" +
            "**回答用户'我有几个角色'类问题时, 直接使用 total 字段, 不要自己数 characters 数组**。")
    public ResonatorSummary getMyResonators(ToolContext toolContext) {
        // ToolContext 是 Spring AI 提供的"隐藏参数"，LLM 看不到它
        // 我们从中取 Controller 传进来的 userId
        Long userId = (Long) toolContext.getContext().get("userId");
        log.info("[Tool] getMyResonators 被 LLM 调用了, userId={}" , userId);
        List<Resonator> list = resonatorService.findAll(userId);
        return new ResonatorSummary(list.size(), list);
    }
}

