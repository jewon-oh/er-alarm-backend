package com.eralarm.eralarmbackend.fcm_token;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// todo: member에 통합시켜야함
@Entity
@Getter
@NoArgsConstructor
@Table(name = "fcm_token")
public class FcmToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 10, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Builder
    public FcmToken(String token) {
        this.token = token;
    }
}
