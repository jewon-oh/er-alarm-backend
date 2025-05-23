package com.eralarm.eralarmbackend.push_notification;

import com.eralarm.eralarmbackend.message.MqMessageTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.eralarm.eralarmbackend.rabbitmq.RabbitMqConfig.FCM_EXCHANGE;
import static com.eralarm.eralarmbackend.rabbitmq.RabbitMqConfig.FCM_ROUTING_KEY;

@Component
@Slf4j
@RequiredArgsConstructor
public class PushNotificationTriggerScheduler {
    private final RabbitTemplate rabbitTemplate;

    // 매일 오후 6시 알림 예약
    @Scheduled(cron = "0 0 18 * * *")
    //@Scheduled(cron = "*/4 * * * * *")
    public void sendPushStartSignal() {
        log.info("message sent: {}", "푸시 알림 시작 트리거 작동");
        rabbitTemplate.convertAndSend(FCM_EXCHANGE, FCM_ROUTING_KEY, MqMessageTypes.START_PUSH.toString());
    }
}