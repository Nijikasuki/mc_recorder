package com.dy.mcrecorder.mc_recorder.common;

// TODO 需要的 import 自己加（Lombok 的注解）

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO 1: 类声明
//   - 这是个【泛型类】，类名后面要带类型参数（参考 List<T> 的写法）
//   - 用 Lombok 注解生成：getter/setter、无参构造、全参构造
//     （提示：3 个注解 —— @Data / @NoArgsConstructor / @AllArgsConstructor）
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(0,"success",data);
    }

    public static <T> Result<T> success() {
        return new Result<>(0,"success",null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code,message,null);
    }
}
