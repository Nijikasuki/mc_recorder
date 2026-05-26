package com.dy.mcrecorder.mc_recorder.common;

public final class MqConstants {
    // ===== Exchange =====
    public static final String EVENTS_EXCHANGE = "wuwa.events";

    // ===== Queue 名 =====
    public static final String USER_REGISTERED_QUEUE = "wuwa.user.registered.queue";
    public static final String ORDER_CREATED_QUEUE   = "wuwa.order.created.queue";

    // ===== Routing Key =====
    public static final String USER_REGISTERED_KEY = "user.registered";
    public static final String ORDER_CREATED_KEY   = "order.created";

    private MqConstants() {}   // 工具类，禁止实例化
}