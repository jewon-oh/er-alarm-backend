package com.eralarm.eralarmbackend.push_notification;
import com.eralarm.eralarmbackend.fcm_token.FcmTokenRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final PushNotificationRepository repository;
    private final FcmTokenRepository fcmTokenRepository;

    public void sendMessageTo(String targetToken, String title, String body)  {
        String response = "";
        try{
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            PushNotification pushNotification = new PushNotification(targetToken,title,body);
            repository.save(pushNotification);

            Message message = Message.builder()
                    .setToken(targetToken)
                    .setNotification(notification)
                    .build();

            response = FirebaseMessaging.getInstance().send(message);
            log.info("✅ 메시지 전송 완료: " + response);
        }catch (FirebaseMessagingException e){
            log.info(e.getMessage());
            fcmTokenRepository.deleteByToken(targetToken);
        }
    }
}

