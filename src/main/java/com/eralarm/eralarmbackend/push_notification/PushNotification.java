package com.eralarm.eralarmbackend.push_notification;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "push_notification",  schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class PushNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String title;

    private String message;

    @Builder
    public PushNotification(String token, String title, String message) {
        this.token = token;
        this.title = title;
        this.message = message;
    }
}
