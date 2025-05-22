package com.eralarm.eralarmbackend.push_notification;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "push_notification",  schema = "public")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
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
