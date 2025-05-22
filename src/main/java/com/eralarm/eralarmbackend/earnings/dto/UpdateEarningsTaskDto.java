package com.eralarm.eralarmbackend.earnings.dto;

import com.eralarm.eralarmbackend.earnings.EarningsTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateEarningsTaskDto {
    private String taskId;
    private EarningsTaskStatus status;
    private String message;
}
