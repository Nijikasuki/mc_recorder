package com.dy.mcrecorder.mc_recorder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kurobbs")
public class KurobbsProperties {
    private String baseUrl = "https://api.kurobbs.com";
    private String did;
    private String source = "android";
    private String userAgent;
    private String origin = "https://web-static.kurobbs.com"; //Origin Header
    private Integer gameId = 3;
    private String serverId;
    private Integer channelId = 19;
    private Integer countryCode = 1; //国服
}
