package com.dy.mcrecorder.mc_recorder.dto.kurobbs;

/**
 * 库街区通用响应信封
 * 注意: data 字段是 JSON 字符串, 不是直接对象, 需要再解析一次
 */
public record KurobbsResponse(
        int code,
        String msg,
        String data,
        Boolean success
) {
    public boolean isOk() {
        return code == 200;
    }
    public boolean isTokenExpired() {
        return code == 10903;     // 我们测出来的真实失效码
    }
}