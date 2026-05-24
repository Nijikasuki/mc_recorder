package com.dy.mcrecorder.mc_recorder.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// @RestController：标记这是个 Controller（接待员），且方法返回值自动转成 JSON 写回浏览器
//   启动时被组件扫描发现 → 注册成 Spring Bean → 方法上的 URL 登记进路由表
// 这个类是入门练习用的，和业务（角色）无关
@RestController
@Tag(name = "测试 - Hello", description = "入门练习用的简单接口")
public class HelloController {

    // @GetMapping("/wuwa")：GET 请求 /wuwa 时，调用这个方法
    // 方法名(hello) 随便起，Spring 只认注解里的 URL
    @GetMapping("/wuwa")
    @Operation(summary = "Hello 鸣潮", description = "返回一句问候字符串")
    public String hello() {
        return "Hello, 鸣潮！";   // 字符串直接成为 HTTP 响应体
    }

    @GetMapping("/mc")
    @Operation(summary = "Hello mc")
    public String mc() {
        return "hello mc";
    }

    // @PostMapping("/echo")：POST 请求 /echo 时调用
    // @RequestBody String message：把请求体(body)当成纯文本，整个塞进 message
    //   —— 对比 ResonatorController 里的 @RequestBody Resonator，那个会把 JSON 反序列化成对象
    @PostMapping("/echo")
    @Operation(summary = "回声", description = "body 是纯文本，原样带前缀返回")
    public String echo(@RequestBody String message) {
        return "你发来的消息是: " + message;
    }
}
