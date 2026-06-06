package com.dy.mcrecorder.mc_recorder.exception;

import com.dy.mcrecorder.mc_recorder.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 全局异常"翻译官"：盯着所有 Controller，把抛出来的异常统一翻译成 Result 信封
// @RestControllerAdvice = @ControllerAdvice + @ResponseBody（返回值自动转 JSON，和 @RestController 一样）
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 专接"@Valid 校验失败"这种异常（缺字段、超范围等都是它）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException e) {
        // 从异常里掏出"第一个字段错误"的提示语（就是实体上 message="xxx" 那句）
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.fail(400, message);
    }

    @ExceptionHandler(BizException.class)
    public Result<Void> handleBiz(BizException e) {
        // 业务异常不打 ERROR 日志, 用 WARN 或 DEBUG 即可
        log.warn("业务异常: {}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 兜底：其它没预料到的异常，统一返回 500，绝不把堆栈泄露给前端
    @ExceptionHandler(Exception.class)
    public Result<Void> handleOther(Exception e) {
        // 真实项目这里要打日志（log.error），方便开发者排查；但响应只给一句人话
        log.error("未捕获异常: ", e);// ← 第二个参数传 e,SLF4J 自动打全 stack trace
        return Result.fail(500, "服务器内部错误");
    }
}
