package com.eralarm.eralarmbackend.fcm_token;

import com.eralarm.eralarmbackend.base.rest_api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
// todo: spring security 적용 후 cors 설정 일괄 적용 필요
@CrossOrigin(originPatterns = {"http://localhost*","https://eralarm*",
"http://192.168.0.*"},
        maxAge = 3600,
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class FcmTokenController {
    private final FcmTokenRepository tokenRepository;

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> registerToken(@RequestBody FcmTokenRequest fcmTokenRequest) {
        FcmToken fcmToken = new FcmToken(fcmTokenRequest.getToken());
        log.info("fcm 토큰 새로 등록 : {}", fcmToken);
        tokenRepository.save(fcmToken);
        return ApiResponse.ok(fcmToken.toString());
    }
}
