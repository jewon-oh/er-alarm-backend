package com.eralarm.eralarmbackend.notification;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final PersonalNotificationRepository repository;

    public void sendMessageTo(String targetToken, String title, String body) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        PersonalNotification personalNotification = new PersonalNotification(targetToken,title,body);
        repository.save(personalNotification);

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(notification)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("✅ 메시지 전송 완료: " + response);
    }
}

