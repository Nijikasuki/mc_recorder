package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.common.MqConstants;
import com.dy.mcrecorder.mc_recorder.dto.UserRegisteredEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class WelcomeEmailConsumer {
    @RabbitListener(queues = MqConstants.USER_REGISTERED_QUEUE)
    public void onUserRegistered(UserRegisteredEvent event) {
        // 假装发邮件 —— 真要发就把这块换成 JavaMailSender 调用
        System.out.println("====== [假装在发欢迎邮件] ======");
        System.out.println("收件人: " + event.getEmail());
        System.out.println("内容: 欢迎 " + event.getUsername() + " 加入鸣潮记录工具！");
        System.out.println("==============================");
    }
}
