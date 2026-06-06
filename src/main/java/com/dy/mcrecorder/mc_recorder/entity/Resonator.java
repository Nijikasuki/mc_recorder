package com.dy.mcrecorder.mc_recorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "共鸣者（角色）")
@TableName("resonator")
public class Resonator {

    // 整数全用包装类(Long/Integer)不用基本类型(long/int)：包装类能为 null，数据库列可能是 NULL
    @Schema(description = "主键 ID（新增时不填，数据库自增）", accessMode = Schema.AccessMode.READ_ONLY)
    @TableId(type = IdType.AUTO)
    private Long id;                    // 对应列 id（自增主键，新增时不用填）

    @Schema(description = "归属用户 ID（后端自动填，请求里不要传）", hidden = true)
    private Long ownerId;

    @Schema(description = "角色名", example = "今汐")
    @NotBlank(message="名字不能为空")
    private String name;                // 对应列 name

    @Schema(description = "属性：衍射/湮灭/气动/热熔/冷凝/导电", example = "衍射")
    @NotBlank(message="属性不能为空")
    private String element;             // 对应列 element（属性：衍射/湮灭/...）

    @Schema(description = "星级", example = "5", minimum = "4", maximum = "5")
    @NotNull(message="星级不能为空")@Min(4)@Max(5)
    private Integer star;               // 对应列 star（星级 4 或 5）

    @Schema(description = "等级", example = "80", minimum = "1", maximum = "90")
    @NotNull(message="等级不能为空")@Min(1)@Max(90)
    private Integer level;              // 对应列 level（等级 1-90）

    // 字段是驼峰 resonanceChain，数据库列是下划线 resonance_chain
    // 靠 application.yaml 里 map-underscore-to-camel-case: true 自动互转
    @Schema(description = "共鸣链等级", example = "2", minimum = "0", maximum = "6")
    @NotNull(message="共鸣链不能为空") @Min(0) @Max(6)
    private Integer resonanceChain;     // 对应列 resonance_chain（共鸣链 0-6）

    @Schema(description = "库街区角色模板 ID", example = "1509")
    private Integer kuroRoleId;

    @Schema(description = "数据来源: manual 手动 / kurobbs 同步", example = "kurobbs")
    private String source;

    @Schema(description = "上次从库街区同步时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime syncedAt;

    @Schema(description = "角色头像 URL", accessMode = Schema.AccessMode.READ_ONLY)
    private String roleIconUrl;

    @Schema(description = "角色立绘 URL", accessMode = Schema.AccessMode.READ_ONLY)
    private String rolePicUrl;

    // 这两个时间字段：建表时配了 DEFAULT/ON UPDATE CURRENT_TIMESTAMP
    // 由数据库自动维护，INSERT/UPDATE 的 SQL 里都不写它们
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;    // 对应列 created_at

    @Schema(description = "最后更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;    // 对应列 updated_at

    @Schema(description = "突破等级", example = "6")
    private Integer breach;

    @Schema(description = "是否主角(漂泊者)")
    private Boolean isMainRole;

    @Schema(description = "武器类型", example = "迅刀")
    private String weaponTypeName;

    @Schema(description = "当前皮肤名", example = "缤纷映色")
    private String skinName;

    @Schema(description = "皮肤立绘 URL", accessMode = Schema.AccessMode.READ_ONLY)
    private String skinPicUrl;
}
