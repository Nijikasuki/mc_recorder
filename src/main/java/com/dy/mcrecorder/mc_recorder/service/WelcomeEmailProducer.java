package com.dy.mcrecorder.mc_recorder.service;

import com.dy.mcrecorder.mc_recorder.common.MqConstants;
import com.dy.mcrecorder.mc_recorder.dto.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class WelcomeEmailProducer {
    private final RabbitTemplate rabbitTemplate;

    public WelcomeEmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendWelcome(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(
                MqConstants.EVENTS_EXCHANGE,
                MqConstants.USER_REGISTERED_KEY,
                event
        );
        System.out.println("[Producer] 已发送注册事件: " + event);
    }
}
