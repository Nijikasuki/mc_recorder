package com.dy.mcrecorder.mc_recorder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("tavily")
public class TavilyProperties {
    private String baseUrl;
    private String apiKey;
}
