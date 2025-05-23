package com.eralarm.eralarmbackend.earnings.service;

import com.eralarm.eralarmbackend.earnings.repository.EarningsRepository;
import com.eralarm.eralarmbackend.earnings.entity.Earnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EarningsDomainService {
    private final EarningsRepository repository;

    public List<Earnings> getAllEarnings() {
        return repository.findAll();
    }

    // get Earnings if date is now
    public List<Earnings> getEarningsToday(){
        // 오늘 자정
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        OffsetDateTime startOfDay = today.atStartOfDay().atOffset(ZoneOffset.UTC);
        // 오늘 23:59:59.999
        OffsetDateTime endOfDay = today.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);

        return repository.findAllByEarningsDateBetween(startOfDay, endOfDay);
    }

    public Earnings findById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
