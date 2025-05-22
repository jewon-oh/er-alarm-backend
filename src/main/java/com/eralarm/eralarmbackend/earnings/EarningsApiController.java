package com.eralarm.eralarmbackend.earnings;


import com.eralarm.eralarmbackend.base.rest_api.ApiResponse;

import com.eralarm.eralarmbackend.earnings.dto.EarningsMultipleResponse;
import com.eralarm.eralarmbackend.earnings.dto.EarningsSingleResponse;
import com.eralarm.eralarmbackend.earnings.dto.EarningsSingleWithSubResponse;
import com.eralarm.eralarmbackend.earnings.dto.EarningsUpdateRequest;

import com.eralarm.eralarmbackend.earnings.service.EarningsService;

import com.eralarm.eralarmbackend.earnings.dto.SubscriptionRequest;
import com.eralarm.eralarmbackend.earnings.service.SubscribedEarningsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/earnings")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(originPatterns = {"http://localhost*", "https://eralarm*",
        "http://192.168.0.*"},
        maxAge = 3600,
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class EarningsApiController {
    private final EarningsService earningsService;
    private final SubscribedEarningsService subscribedEarningsService;

//    @PostMapping("/update")
//    public ResponseEntity<ApiResponse<String>> requestEarningsUpdate(@RequestBody EarningsUpdateRequest request) {
//        String taskId = earningsTaskService.requestEarningsUpdate(request.getSymbol());
//
//        log.info("🔁 실적 요청 전송: {} (taskId: {})", request.getSymbol(), taskId);
//
//        return ApiResponse.ok("success",taskId);
//    }

    /**
     * 2. Python 서버가 callback으로 결과 전송 (earningsDates 포함)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EarningsMultipleResponse>> receiveEarningsResult(@RequestBody EarningsUpdateRequest request) {
        log.info("📬 실적 결과 수신: {}, {}개 날짜", request.getSymbol(), request.getEarningsDates().size());
        return ApiResponse.ok(earningsService.updateEarningsDate(request));
    }

    @GetMapping(path = "{symbol}")
    public ResponseEntity<ApiResponse<Page<EarningsSingleWithSubResponse>>> getEarningsWithoutSub(@PathVariable String symbol, @PageableDefault(size = 10) Pageable pageable) {
        log.info("{} 실적 조회",symbol);
        return ApiResponse.ok(earningsService.getEarnings(symbol,pageable));
    }

    @GetMapping(path="{symbol}" ,headers = "FcmToken")
    public ResponseEntity<ApiResponse<Page<EarningsSingleWithSubResponse>>> getEarningsWithSub(@PathVariable String symbol, @RequestHeader HttpHeaders headers, @PageableDefault(size = 10) Pageable pageable) {
        log.info("{} 실적 조회",symbol);
        String fcmToken = headers.getFirst("FcmToken");
        log.info("fcm token: {}",fcmToken);
        if(fcmToken==null) {
           return ApiResponse.ok(earningsService.getEarnings(symbol,pageable));
        }else{
            return ApiResponse.ok(earningsService.getEarningsByFcmToken(fcmToken,symbol,pageable));
        }

    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<Page<EarningsSingleResponse>>> getEarningsToday(@PageableDefault(size = 10) Pageable pageable) {
        log.info("오늘 실적 발표하는 주식 조회");
        return ApiResponse.ok(earningsService.getEarningsToday(pageable));
    }

    @PostMapping("/{symbol}/subscribe")
    public ResponseEntity<ApiResponse<Object>> requestSubscribePushNotification(@PathVariable String symbol, @RequestBody SubscriptionRequest request) {
        log.info("{} 실적 발표 알림 구독 {}", symbol, request.getFcmToken());
        subscribedEarningsService.subscribe(request);

        return ApiResponse.ok();
    }

    @PutMapping("/{symbol}/unsubscribe")
    public ResponseEntity<ApiResponse<Object>> requestUnsubscribePushNotification(@PathVariable String symbol, @RequestBody SubscriptionRequest request){
        log.info("{} 실적 발표 알림 구독 취소 {}", symbol, request.getFcmToken());
        subscribedEarningsService.unsubscribe(request);

        return ApiResponse.ok();
    }
}
