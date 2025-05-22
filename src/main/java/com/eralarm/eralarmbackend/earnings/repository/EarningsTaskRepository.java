package com.eralarm.eralarmbackend.earnings.repository;

import com.eralarm.eralarmbackend.earnings.EarningsTask;
import com.eralarm.eralarmbackend.earnings.EarningsTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EarningsTaskRepository extends JpaRepository<EarningsTask, Long> {
    List<EarningsTask> findByStatusNot(EarningsTaskStatus status); // SUCCESS가 아닌 task 추적용

    Optional<EarningsTask> findByTaskId(String taskId);
}
