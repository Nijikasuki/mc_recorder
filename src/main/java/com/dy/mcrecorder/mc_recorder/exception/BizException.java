package com.dy.mcrecorder.mc_recorder.exception;

import lombok.Getter;

/**
 * 业务异常 — Service 层用它表达"业务规则不通过", 不是系统 bug
 * GlobalExceptionHandler 会翻译成 Result.fail(code, message)
 */

@Getter
public class BizException extends RuntimeException {
    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message) {
        this(400, message);   // 不指定 code 默认 400
    }
}