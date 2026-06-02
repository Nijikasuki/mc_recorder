package com.dy.mcrecorder.mc_recorder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("github")
public class GitHubProperties {
    private String baseUrl;
    private String token;
}
