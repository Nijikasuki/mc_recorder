package com.dy.mcrecorder.mc_recorder.mapper;

import com.dy.mcrecorder.mc_recorder.entity.Resonator;
import org.apache.ibatis.annotations.*;

import java.util.List;

// Mapper = 对【数据库】的接口（Controller 是对【前端】的接口，方向相反）
//
// @Mapper：告诉 MyBatis 扫描这个接口，运行时用"动态代理"凭空生成实现类，
//          再把实现类对象注册成 Spring Bean。
// 所以：① 这是 interface，没有方法体（实现交给 MyBatis）
//      ② 你不能 new 它，靠构造注入拿到 MyBatis 造的那个对象
//
// 占位符规则：
//   #{xxx} 安全（参数化，防 SQL 注入）—— 业务一律用它
//   ${xxx} 危险（字符串拼接）—— 只用于动态表名/列名
//   单个对象参数时，#{name} 自动取 resonator.getName()
@Mapper
public interface ResonatorMapper {

    // 查全部：返回 List<Resonator>，MyBatis 把每行映射成一个对象塞进 List
    @Select("SELECT * FROM resonator")
    List<Resonator> findAll();

    // 按 id 查一条：查到返回对象，查不到返回 null
    @Select("SELECT * FROM resonator WHERE id = #{id}")
    Resonator findById(Long id);

    // 新增：SQL 里不写 id（自增）、created_at/updated_at（数据库默认值）
    // @Options(useGeneratedKeys=true, keyProperty="id")：
    //   把数据库生成的自增主键，回填到传入对象的 id 字段
    //   —— 调用后该对象就带着新 id（上层据此把新 id 返回给前端）
    @Insert("INSERT INTO resonator (name, element, star, level, resonance_chain) VALUES (#{name}, #{element}, #{star}, #{level}, #{resonanceChain})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Resonator resonator);   // 返回值 = 受影响行数（成功为 1）

    // 编辑：只更新业务字段，updated_at 由数据库 ON UPDATE 自动刷新
    // 单参数(Resonator)，所以 #{name}/#{id} 都自动从这一个对象取（多参数会绑定失败）
    @Update("UPDATE resonator SET name=#{name}, element=#{element}, star=#{star}, level=#{level}, resonance_chain=#{resonanceChain} WHERE id = #{id}")
    int update(Resonator resonator);

    @Delete("DELETE FROM resonator WHERE id = #{id}")
    int delete(Long id);
}
