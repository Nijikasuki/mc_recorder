package com.dy.mcrecorder.mc_recorder.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dy.mcrecorder.mc_recorder.common.JwtUtil;
import com.dy.mcrecorder.mc_recorder.entity.User;
import com.dy.mcrecorder.mc_recorder.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public UserService(UserMapper mapper, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.mapper = mapper;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public boolean register(User user) {
        LambdaQueryWrapper<User> q = new LambdaQueryWrapper<>();
        q.eq(User::getUsername, user.getUsername());
        if(mapper.selectOne(q) != null){
            return false;
        }
        else{
            String hash = encoder.encode(user.getPassword());
            user.setPassword(hash);
            mapper.insert(user);
            return true;
        }
    }

    public String login(User user) {
        LambdaQueryWrapper<User> q = new LambdaQueryWrapper<User>();
        q.eq(User::getUsername, user.getUsername());
        User dbUser = mapper.selectOne(q);
        if(dbUser == null){
            return null; //用户不存在
        }
        if(!encoder.matches(user.getPassword(), dbUser.getPassword())){
            return null; //密码错误
        }
        String token = jwtUtil.generateToken(dbUser.getId());
        return token;
    }

    public User findById(Long id) {
        return mapper.selectById(id);
    }

}
