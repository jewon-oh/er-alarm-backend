package com.eralarm.eralarmbackend.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "personal_notification",  schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalNotification {
    @Id
    private String vapid;

    private String title;

    private String message;
}
