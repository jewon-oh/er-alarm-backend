package com.eralarm.eralarmbackend.earnings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaveEarningsTaskDto {
    private String taskId;
    private String symbol;
}
