package com.dy.mcrecorder.mc_recorder.entity;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private LocalDateTime createdAt;    // 对应列 created_at
    private LocalDateTime updatedAt;    // 对应列 updated_at
}
