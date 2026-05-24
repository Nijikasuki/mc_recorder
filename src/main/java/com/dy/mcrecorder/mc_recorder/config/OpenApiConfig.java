package com.dy.mcrecorder.mc_recorder.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

// Swagger / OpenAPI 配置：告诉 Swagger UI 我们用 Bearer JWT 认证
// 这样页面右上角的 Authorize 按钮才能用，受保护接口才能在网页上调通

@Configuration
@OpenAPIDefinition(
        // 文档基本信息（可选，但写上更专业）
        info = @Info(title = "mc_recorder API", version = "1.0", description = "鸣潮记录工具接口文档"),
        // 全局默认所有接口都需要 bearerAuth 认证（避免每个接口单独标）
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",               // 这个名字要和上面 SecurityRequirement 里的 name 对上
        type = SecuritySchemeType.HTTP,    // 走 HTTP Header 认证
        scheme = "bearer",                 // 具体方案是 Bearer Token
        bearerFormat = "JWT"               // 文档里告诉读者：token 是 JWT 格式
)
public class OpenApiConfig {
}
