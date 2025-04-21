package com.eralarm.eralarmbackend.fcm_token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
// todo: spring security 적용 후 cors 설정 일괄 적용 필요
@CrossOrigin(originPatterns = {"http://localhost*","https://eralarm.local.fresh96jwjw.org",
"http://192.168.0.129:8080"},
        maxAge = 3600,
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class FcmTokenController {
    private final FcmTokenRepository tokenRepository;

    @PostMapping("")
    public ResponseEntity<String> registerToken(@RequestBody FcmTokenRequest fcmTokenRequest) {
        FcmToken fcmToken = new FcmToken(fcmTokenRequest.getToken());
        log.info("FcmToken: {}", fcmToken);
        tokenRepository.save(fcmToken);
        return ResponseEntity.ok(fcmToken.toString());
    }
}
