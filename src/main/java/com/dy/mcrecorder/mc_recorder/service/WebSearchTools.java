package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.dto.TavilySearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebSearchTools {
    private TavilyService tavilyService;
    public WebSearchTools(TavilyService tavilyService) {
        this.tavilyService = tavilyService;
    }

    @Tool(description = """
              搜索互联网, 查询任何外部公开信息(新闻、文章、文档、知识等)。
              适用场景: 用户问的问题在 [参考资料] 中没有, 也不属于"用户业务数据"或"GitHub 仓库"时使用。
              例如: '今天 ChatGPT 发布了什么新功能', '什么是 LangGraph'
              参数:
                - query: 搜索关键词, 越具体越好
              """)
    public TavilySearchResponse webSearch(String query) {
        log.info("[Tool] webSearch tools 被 LLM 调用:{}", query);
        return tavilyService.search(query);
    }
}
