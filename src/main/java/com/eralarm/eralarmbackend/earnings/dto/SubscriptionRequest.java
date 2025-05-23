package com.eralarm.eralarmbackend.earnings.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class SubscriptionRequest {
    private final String fcmToken;
    private final Long earningsId;
}