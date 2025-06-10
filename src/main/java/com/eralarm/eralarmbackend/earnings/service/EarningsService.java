package com.eralarm.eralarmbackend.earnings.service;

import com.eralarm.eralarmbackend.earnings.repository.EarningsRepository;
import com.eralarm.eralarmbackend.earnings.dto.*;
import com.eralarm.eralarmbackend.earnings.entity.Earnings;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EarningsService {
    @PersistenceContext
    private final EntityManager em;
    private final EarningsRepository earningsRepository;

    public Page<EarningsSingleWithSubResponse> getEarnings(String symbol,Pageable pageable) {
        Page<Earnings> earnings = earningsRepository.findAllBySymbolIgnoreCase(symbol,pageable);

        return earnings.map(entity ->
                EarningsSingleWithSubResponse.builder()
                        .id(entity.getId())
                        .symbol(entity.getSymbol())
                        .earningsDate(entity.getEarningsDate())
                        .epsActual(entity.getEpsActual())
                        .epsEstimate(entity.getEpsEstimate())
                        .surprisePct(entity.getSurprisePct())
                        .subscribed(false)
                        .build());
    }

//    public Page<EarningsSingleWithSubResponse> getEarningsByFcmToken(String fcmToken,String symbol,Pageable pageable) {
//        return earningsRepository.findAllWithSubscriptionBySymbol(fcmToken,symbol,pageable);
//    }
    public Page<EarningsSingleWithSubResponse> getEarningsByFcmToken(String fcmToken,String symbol, String date,Pageable pageable) {
        if(date == null || date.isEmpty()) {
            return earningsRepository.findAllWithSubscription(fcmToken,symbol,null,null,pageable);
        }

        // 1) LocalDate 로 파싱
        LocalDate localDate = LocalDate.parse(date);

        // 2) 해당 날짜의 시작(00:00) / 다음날 시작 시각 생성
        OffsetDateTime startOfDay = localDate.atStartOfDay().atOffset(ZoneOffset.ofHours(9));
        OffsetDateTime endOfDay = localDate.atTime(LocalTime.MAX).atOffset(ZoneOffset.ofHours(9));

        return earningsRepository.findAllWithSubscription(fcmToken,null,startOfDay,endOfDay,pageable);
    }

    public Page<EarningsSingleResponse> getEarningsToday(Pageable pageable){
        // 오늘 자정
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        OffsetDateTime startOfDay = today.atStartOfDay().atOffset(ZoneOffset.ofHours(9));
        // 오늘 23:59:59.999
        OffsetDateTime endOfDay = today.atTime(LocalTime.MAX).atOffset(ZoneOffset.ofHours(9));
        Page<Earnings> earnings = earningsRepository.findAllPageByEarningsDateBetween(startOfDay, endOfDay,pageable);

        log.info("{}",earnings.getNumberOfElements());

        return earnings.map(entity ->
                new EarningsSingleResponse(entity.getId(),entity.getSymbol(), entity.getEarningsDate()));
    }

    @Transactional
    public EarningsMultipleResponse updateEarningsDate(EarningsUpdateRequest request) {
        String symbol = request.getSymbol();

        // 1) 기존 날짜 리스트를 Set으로 변환
        Set<OffsetDateTime> existingSet = new HashSet<>(earningsRepository.findEarningsDatesBySymbolIgnoreCase(request.getSymbol()));

        // 2) 한 번의 루프로 신규 날짜만 삽입 대상에 추가
        List<Earnings> toInsert = new ArrayList<>();
        for (EarningsInfoDto dto : request.getEarningsList()) {
            if (!existingSet.contains(dto.getDate())) {
                Earnings newEarnings = Earnings.builder()
                        .symbol(symbol)
                        .earningsDate(dto.getDate())
                        .epsEstimate(dto.getEpsEstimate())
                        .epsActual(dto.getEpsActual())
                        .surprisePct(dto.getSurprisePct())
                        .build();
                toInsert.add(newEarnings);
            }
        }
        if(toInsert.isEmpty()) {
            log.info("❌ 새로운 실적 발표일이 없습니다: {}", symbol);
            List<OffsetDateTime> existingDates = earningsRepository.findAllBySymbolIgnoreCase(symbol).stream()
                    .map(Earnings::getEarningsDate)
                    .toList();
            return new EarningsMultipleResponse(symbol, existingDates);
        }

        toInsert.forEach(em::persist);
        em.flush();
        em.clear();

        log.info("✅ 실적 {}개 저장 완료.", toInsert.size());

        List<OffsetDateTime> earningsDateDtoList = earningsRepository.findAllBySymbolIgnoreCase(symbol).stream().map(Earnings::getEarningsDate).toList();
        return new EarningsMultipleResponse(symbol,earningsDateDtoList);
    }
}
