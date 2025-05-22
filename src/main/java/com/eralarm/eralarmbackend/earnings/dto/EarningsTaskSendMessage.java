package com.eralarm.eralarmbackend.earnings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class EarningsTaskSendMessage {
    private String symbol;
    private String taskId;
}
