package com.eralarm.eralarmbackend.push_notification;

import com.eralarm.eralarmbackend.earnings.service.EarningsDomainService;
import com.eralarm.eralarmbackend.earnings.entity.Earnings;
import com.eralarm.eralarmbackend.fcm_token.FcmTokenRepository;
import com.eralarm.eralarmbackend.message.MqMessageTypes;
import com.eralarm.eralarmbackend.earnings.SubscribedEarnings;
import com.eralarm.eralarmbackend.earnings.service.SubscribedEarningsService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.eralarm.eralarmbackend.rabbitmq.RabbitMqConfig.FCM_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushNotificationConsumer {
    private final PushNotificationService pushNotificationService;
    private final FcmTokenRepository fcmTokenRepository;
    private final EarningsDomainService earningsDomainService;
    private final SubscribedEarningsService subscribedEarningsService;
    /**
     * Queue에서 메시지를 구독
     *
     * @param messagePayload 구독한 메시지를 담고 있는 MessagePayload 객체
     */
    @RabbitListener(queues = FCM_QUEUE)
    public void receiveMessage(MqMessageTypes messagePayload) throws FirebaseMessagingException {
        log.info("수신한 메세지: {}", messagePayload);
        if(messagePayload == MqMessageTypes.START_PUSH) {
            // todo: push dispatcher, push worker 나눠야 함
            log.info("🔔 푸시 전송 시작");

            List<Earnings> earningsList = earningsDomainService.getEarningsToday();

            for(Earnings earnings : earningsList) {
                log.info(earnings.getSymbol());
                List<String> fcmTokens = subscribedEarningsService.getAllSubscribedEarnings(earnings.getId()).stream().map(SubscribedEarnings::getFcmToken).toList();

                // 푸시 알림 전송
                for (String fcmToken : fcmTokens) {
                    log.info(fcmToken);
                    pushNotificationService.sendMessageTo(fcmToken,earnings.getSymbol()+"실적 발표",earnings.getEarningsDate().toString());
                }
            }
        }
    }
}
