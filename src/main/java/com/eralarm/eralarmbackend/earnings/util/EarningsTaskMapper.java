package com.eralarm.eralarmbackend.earnings.util;

import com.eralarm.eralarmbackend.earnings.dto.EarningsTaskReceiveMessage;
import com.eralarm.eralarmbackend.earnings.dto.EarningsTaskSendMessage;
import com.eralarm.eralarmbackend.earnings.dto.SaveEarningsTaskDto;
import com.eralarm.eralarmbackend.earnings.dto.UpdateEarningsTaskDto;

public class EarningsTaskMapper {
    static public SaveEarningsTaskDto toSaveDto(EarningsTaskSendMessage message) {
        return SaveEarningsTaskDto.builder()
                .taskId(message.getTaskId())
                .symbol(message.getSymbol())
                .build();
    }

    static public UpdateEarningsTaskDto toUpdateDto(EarningsTaskReceiveMessage message) {
        return UpdateEarningsTaskDto.builder()
                .taskId(message.getTaskId())
                .status(message.getStatus())
                .message(message.getMessage())
                .build();

    }
}
