package com.eralarm.eralarmbackend.earnings.service;


import com.eralarm.eralarmbackend.earnings.dto.SubscriptionRequest;
import com.eralarm.eralarmbackend.earnings.entity.Earnings;
import com.eralarm.eralarmbackend.earnings.SubscribedEarnings;
import com.eralarm.eralarmbackend.earnings.repository.SubscribedEarningsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribedEarningsService {
    private final SubscribedEarningsRepository repository;
    private final EarningsDomainService earningsDomainService;

    @Transactional
    public void unsubscribe(SubscriptionRequest request) {
        toggleSubscription(false,request);
        log.info("구독 성공");
    }

    @Transactional
    public void subscribe(SubscriptionRequest request) {
        toggleSubscription(true,request);
        log.info("구독 취소 성공");
    }

    @Transactional
    public void toggleSubscription(Boolean enable,SubscriptionRequest request) {
        Long earningsId = request.getEarningsId();
        String fcmToken = request.getFcmToken();

        repository.findByEarningsIdAndFcmToken(earningsId, fcmToken)
                .ifPresentOrElse(
                        existing -> {
                            // 이미 구독정보가 있으면 subscribe() 하고 save
                            log.info("구독 정보 이미 존재 {}", existing.getId());
                            if(enable) {
                                existing.subscribe();
                            }else{
                                existing.unsubscribe();
                            }
                            repository.save(existing);
                        },
                        () -> {
                            // 없으면 새로 생성해서 save
                            log.info("구독 정보 없음. 새로 생성, earningsId: {}, fcmToken: {}", earningsId, fcmToken);
                            Earnings earnings = earningsDomainService.findById(earningsId);
                            SubscribedEarnings created = SubscribedEarnings.builder()
                                    .earnings(earnings)
                                    .fcmToken(fcmToken)
                                    .build();
                            repository.save(created);
                        }
                );
    }
    public List<SubscribedEarnings> getAllSubscribedEarningsByFcmToken(String fcmToken) {
        return repository.findAllByFcmToken(fcmToken);
    }

    public List<SubscribedEarnings> getAllSubscribedEarnings(Long earningsId) {
        return repository.findAllByEarnings_Id(earningsId);
    }
}
