package com.eralarm.eralarmbackend.earnings.dto;

import com.eralarm.eralarmbackend.earnings.EarningsTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EarningsTaskReceiveMessage {
    private final String taskId;
    private final EarningsTaskStatus status;
    private final String message;
    private String symbol;
    private List<EarningsDateDto> earningsDates;
}
