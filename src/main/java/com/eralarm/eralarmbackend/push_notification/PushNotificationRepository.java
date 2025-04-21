package com.eralarm.eralarmbackend.push_notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Integer> {
}
