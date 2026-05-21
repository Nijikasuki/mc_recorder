package com.dy.mcrecorder.mc_recorder.service;

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
        if(mapper.findByUsername(user.getUsername()) != null){
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
        User dbUser = mapper.findByUsername(user.getUsername());
        if(dbUser == null){
            return null; //用户不存在
        }
        if(!encoder.matches(user.getPassword(), dbUser.getPassword())){
            return null; //密码错误
        }
        String token = jwtUtil.generateToken(dbUser.getId());
        return token;
    }




}
