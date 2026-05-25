package com.dy.mcrecorder.mc_recorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dy.mcrecorder.mc_recorder.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
//    @Insert("INSERT INTO user (username,password) VALUES (#{username},#{password})")
//    int insert(User user);
//
//    @Select("SELECT * FROM user WHERE username = #{username}")
//    User findByUsername(String username);
//
//    @Select("SELECT * FROM user WHERE id = #{id}")
//    User findById(long id);
}
