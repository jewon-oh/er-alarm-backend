package com.eralarm.eralarmbackend.earnings.repository;

import com.eralarm.eralarmbackend.earnings.entity.SubscribedEarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribedEarningsRepository extends JpaRepository<SubscribedEarnings, Long> {
    List<SubscribedEarnings> findAllByFcmToken(String fcmToken);

    Optional<SubscribedEarnings> findByEarningsIdAndFcmToken(Long earningsId, String fcmToken);

    List<SubscribedEarnings> findAllByEarnings_Id(Long earningsId);
}
