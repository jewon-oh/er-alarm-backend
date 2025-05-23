package com.eralarm.eralarmbackend.stock;

import com.eralarm.eralarmbackend.base.rest_api.ApiResponse;
import com.eralarm.eralarmbackend.stock.dto.StockInvalidateRequest;
import com.eralarm.eralarmbackend.stock.dto.UpdateStocksResponse;
import com.eralarm.eralarmbackend.earnings.service.SubscribedEarningsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(originPatterns = {"http://localhost*", "https://eralarm*",
        "http://192.168.0.*"},
        maxAge = 3600,
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class StockApiController {
    private final StockService service;
    private final SubscribedEarningsService subscribedEarningsService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<Stock>>> getStocks(@PageableDefault(size = 10) Pageable pageable) {
        log.info("주식 전체 조회");
        return ApiResponse.ok(service.getAllStocks(pageable));
    }

    @GetMapping("{symbol}")
    public ResponseEntity<ApiResponse<Stock>>  getStock(@PathVariable String symbol) {
        log.info("주식 {} 조회", symbol);
        return ApiResponse.ok(service.getStock(symbol)) ;
    }

    @GetMapping("/find")
    public ResponseEntity<ApiResponse<Page<Stock>>> findStock(@RequestParam String searchParam, @PageableDefault(size = 10) Pageable pageable) {
        log.info("주식 검색 : {}", searchParam);
        return ApiResponse.ok(service.findStock(searchParam, pageable));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<UpdateStocksResponse>> updateStocks() {
        log.info("주식 목록 업데이트");
        UpdateStocksResponse response = service.updateStocks();
        log.info("{}개의 새로운 주식 추가됨", response.getUpdatedStocks());
        return ApiResponse.ok(service.updateStocks());
    }

    @PostMapping("/invalidate")
    public ResponseEntity<ApiResponse<Object>> requestInvalidateStock(@RequestBody StockInvalidateRequest request) {
        log.info("{} 실적 발표 비활성화", request.getSymbol());
        service.invalidate(request);
        return ApiResponse.ok();
    }
}
