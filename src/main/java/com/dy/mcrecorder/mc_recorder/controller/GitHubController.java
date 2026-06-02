package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.dto.GitHubRepo;
import com.dy.mcrecorder.mc_recorder.dto.GitHubRepoActivity;
import com.dy.mcrecorder.mc_recorder.service.GitHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@Tag(name = "4.GitHub 代理")
public class GitHubController {
    private final GitHubService gitHubService;
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/repos/{owner}/{repo}")
    @Operation(summary = "查仓库基本信息", description = "代理调用 GitHub /repos/{owner}/{repo}")
    public Result<GitHubRepo> getRepo(@PathVariable String owner, @PathVariable String repo) {
        return Result.success(gitHubService.getRepo(owner, repo));
    }

    @GetMapping("/repos/{owner}/{repo}/activities")
    @Operation(summary = "查仓库活动列表", description = "代理调用 GitHub /repos/{owner}/{repo}/activity")
    public Result<List<GitHubRepoActivity>> getActivities(@PathVariable String owner, @PathVariable String repo) {
        return Result.success(gitHubService.getRepoActivities(owner, repo));
    }
}
