package com.eralarm.eralarmbackend.earnings.service;

import com.eralarm.eralarmbackend.earnings.EarningsTaskStatus;
import com.eralarm.eralarmbackend.earnings.dto.SaveEarningsTaskDto;

import com.eralarm.eralarmbackend.earnings.EarningsTask;
import com.eralarm.eralarmbackend.earnings.dto.UpdateEarningsTaskDto;
import com.eralarm.eralarmbackend.earnings.repository.EarningsTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EarningsTaskService {
    private final EarningsTaskRepository earningsTaskRepository;

    @Transactional
    public void save(SaveEarningsTaskDto dto) {
        EarningsTask task = EarningsTask.builder()
                .taskId(dto.getTaskId())
                .symbol(dto.getSymbol())
                .status(EarningsTaskStatus.PENDING)
                .build();

        earningsTaskRepository.save(task);
    }

    @Transactional
    public void update(UpdateEarningsTaskDto dto) {
        EarningsTask task = earningsTaskRepository.findByTaskId(dto.getTaskId()).orElseThrow(()->new NoSuchElementException( "존재하지 않는 task "+dto.getTaskId()));
        task.update(dto.getStatus(),dto.getMessage());
    }
}
