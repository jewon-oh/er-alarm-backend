package com.eralarm.eralarmbackend.fcm_token;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// todo: member에 통합시켜야함
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fcm_token")
public class FcmToken implements Serializable {
    @Id
    private String token;
}
