package com.eralarm.eralarmbackend.fcm_token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmTokenDomainService {
    private final FcmTokenRepository repository;

    public boolean isTokenExists(String token) {
        return repository.existsById(token);
    }

    public FcmToken findByToken(String token) {
        return repository.findById(token).orElseThrow(RuntimeException::new);
    }
}
