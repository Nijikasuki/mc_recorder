package com.dy.mcrecorder.mc_recorder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication：一个注解 = 三件事
//   1. 标记这是 Spring Boot 应用的入口
//   2. 开启自动配置（看到 mysql/mybatis 依赖就自动配数据源等）
//   3. 开启组件扫描（扫描本包及子包下的 @RestController/@Mapper 等，注册成 Bean）
//      —— 所以新建的类必须放在 com.dy.mcrecorder.mc_recorder 包或其子包下
@SpringBootApplication
@MapperScan("com.dy.mcrecorder.mc_recorder.mapper")
public class McRecorderApplication {

    // main 是 static：JVM 启动时还没有任何对象，只能用 类名.main() 调用，所以必须 static
    public static void main(String[] args) {
        // 下面是之前测试 Lombok @Data 用的代码，注释留作复习：
        // new 一个对象才能调实例方法（getName 是实例方法，要"某个对象的"名字）
//        Resonator r = new Resonator();
//        r.setName("爱弥斯");      // setName 由 Lombok @Data 自动生成
//        r.setStar(5);
//        r.setLevel(90);
//
//        System.out.println(r.getName());  // 输出：爱弥斯
//        System.out.println(r);            // 输出 toString()，也是 @Data 生成的

        // 真正启动 Spring Boot：内嵌 Tomcat 起来，监听 8080 端口
        SpringApplication.run(McRecorderApplication.class, args);
    }

}
