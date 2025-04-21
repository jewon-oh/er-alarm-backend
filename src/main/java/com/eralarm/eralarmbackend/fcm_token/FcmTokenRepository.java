package com.eralarm.eralarmbackend.fcm_token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken,String > {
}
