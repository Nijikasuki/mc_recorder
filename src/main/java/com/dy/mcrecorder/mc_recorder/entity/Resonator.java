package com.dy.mcrecorder.mc_recorder.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

// 实体类（Entity）：数据库一行 ⇄ 一个 Java 对象
//   resonator 表一行  ⇄  一个 Resonator 对象
//   表里 10 行查出来  ⇄  List<Resonator>（10 个对象）
//
// @Data 是 Lombok 注解，编译时自动生成：
//   - 所有字段的 getter / setter（如 getName()/setName()）
//   - toString() / equals() / hashCode() / 无参构造
// 这些自动生成的方法被两处用到：
//   - MyBatis 查出数据后用 setter 把值填进对象
//   - Jackson 序列化/反序列化 JSON 时用 getter/setter
@Data
public class Resonator {

    // 整数全用包装类(Long/Integer)不用基本类型(long/int)：包装类能为 null，数据库列可能是 NULL
    private Long id;                    // 对应列 id（自增主键，新增时不用填）

    @NotBlank(message="名字不能为空")
    private String name;                // 对应列 name

    @NotBlank(message="属性不能为空")
    private String element;             // 对应列 element（属性：衍射/湮灭/...）

    @NotNull(message="星级不能为空")@Min(4)@Max(5)
    private Integer star;               // 对应列 star（星级 4 或 5）

    @NotNull(message="等级不能为空")@Min(1)@Max(90)
    private Integer level;              // 对应列 level（等级 1-90）

    // 字段是驼峰 resonanceChain，数据库列是下划线 resonance_chain
    // 靠 application.yaml 里 map-underscore-to-camel-case: true 自动互转
    @NotNull(message="共鸣链不能为空") @Min(0) @Max(6)
    private Integer resonanceChain;     // 对应列 resonance_chain（共鸣链 0-6）

    // 这两个时间字段：建表时配了 DEFAULT/ON UPDATE CURRENT_TIMESTAMP
    // 由数据库自动维护，INSERT/UPDATE 的 SQL 里都不写它们
    private LocalDateTime createdAt;    // 对应列 created_at

    private LocalDateTime updatedAt;    // 对应列 updated_at
}
