package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.dto.GitHubRepo;
import com.dy.mcrecorder.mc_recorder.dto.GitHubRepoActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class GitHubTools {

    private final GitHubService gitHubService;

    public GitHubTools(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Tool(description = """
              查询 GitHub 仓库的基本信息, 包括 star 数、fork 数、主要语言、描述、默认分支等。
              适用场景: 用户问'某个仓库怎么样', '多少 star', '什么语言写的'等问题时调用。
              参数说明:
                - owner: 仓库所有者的 GitHub 用户名或组织名(如 'spring-projects', 'vuejs')
                - repo:  仓库名(如 'spring-ai', 'core')
              """)
    public GitHubRepo getRepoInfo(String owner, String repo) {
        log.info("[Tool] getRepoInfo 被 LLM 调用: {}/{}", owner, repo);
        return gitHubService.getRepo(owner, repo);
    }

    @Tool(description = """
              查询 GitHub 仓库最近的活动历史, 包括推送、合并、分支创建/删除等事件。
              适用场景: 用户问'最近这个项目在搞什么', '最新动态'等问题时调用。
              参数说明:
                - owner: 仓库所有者
                - repo:  仓库名
              返回多条活动记录, 每条含时间戳、活动类型、操作人、相关分支等
              """)
    public List<GitHubRepoActivity> getRepoActivities(String owner, String repo) {
        log.info("[Tool] getRepoActivities 被 LLM 调用: {}/{}", owner, repo);
        return gitHubService.getRepoActivities(owner, repo);
    }
}