package com.eralarm.eralarmbackend.push_notification;

import com.eralarm.eralarmbackend.message.MqMessageTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PushNoificationTriggerScheduler {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    // 매일 오후 6시 알림 예약
    @Scheduled(cron = "0 0 18 * * *")
    //@Scheduled(cron = "*/4 * * * * *")
    public void sendPushStartSignal() {
        log.info("message sent: {}", "푸시 알림 시작 트리거 작동");
        rabbitTemplate.convertAndSend(exchangeName, routingKey, MqMessageTypes.START_PUSH.toString());
    }
}