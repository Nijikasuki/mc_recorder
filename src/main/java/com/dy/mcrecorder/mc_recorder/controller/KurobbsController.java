package com.dy.mcrecorder.mc_recorder.controller;

import com.dy.mcrecorder.mc_recorder.common.Result;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.BindRequest;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.KurobbsRole;
import com.dy.mcrecorder.mc_recorder.dto.kurobbs.SyncResult;
import com.dy.mcrecorder.mc_recorder.entity.KuroToken;
import com.dy.mcrecorder.mc_recorder.service.KurobbsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/kurobbs")
@Tag(name = "4. 库街区集成")
public class KurobbsController {
    private final KurobbsService service;

    public KurobbsController(KurobbsService service) {
        this.service = service;
    }

    @PostMapping("/bind")
    @Operation(summary = "接受绑定请求，将用户的token存入数据库")
    public Result<Void> kurobind(@Valid @RequestBody BindRequest bindRequest,@AuthenticationPrincipal Long userId) {
        KuroToken kuroToken = new KuroToken();
        kuroToken.setBatToken(bindRequest.getBatToken());
        kuroToken.setGameRoleId(bindRequest.getGameRoleId());
        kuroToken.setUserId(userId);
        kuroToken.setBoundAt(LocalDateTime.now());
        service.bind(kuroToken);
        return Result.success();
    }

    @GetMapping("/characters")
    @Operation(summary = "利用库街区的Token查我的鸣潮角色信息")
    public Result<List<KurobbsRole>> getCharacters(@AuthenticationPrincipal Long userId) {
        return Result.success(service.getRoles(userId));
    }

    @PostMapping("/sync")
    @Operation(summary = "利用库街区的Token同步本地数据库")
    public Result<SyncResult> sync(@AuthenticationPrincipal Long userId){
        return Result.success(service.syncToResonator(userId));
    }
}
