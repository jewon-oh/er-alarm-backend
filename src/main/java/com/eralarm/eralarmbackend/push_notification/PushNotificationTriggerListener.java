package com.eralarm.eralarmbackend.push_notification;

import com.eralarm.eralarmbackend.fcm_token.FcmToken;
import com.eralarm.eralarmbackend.fcm_token.FcmTokenRepository;
import com.eralarm.eralarmbackend.message.MqMessageTypes;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushNotificationTriggerListener {
    private final PushNotificationService pushNotificationService;
    private final FcmTokenRepository fcmTokenRepository;
    /**
     * Queue에서 메시지를 구독
     *
     * @param messagePayload 구독한 메시지를 담고 있는 MessagePayload 객체
     */
    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void receiveMessage(MqMessageTypes messagePayload) throws FirebaseMessagingException {
        log.info("Received message: {}", messagePayload);
        if(messagePayload == MqMessageTypes.START_PUSH) {
            // todo: push dispatcher, push worker 나눠야 함
            log.info("🔔 푸시 전송 시작");
            // 사용자 조회
            List<FcmToken> fcmTokens = fcmTokenRepository.findAll();

            // 푸시 알림 전송
            for (FcmToken fcmToken : fcmTokens) {
                pushNotificationService.sendMessageTo(fcmToken.getToken(),"메세지","내용");
            }
        }
    }
}
