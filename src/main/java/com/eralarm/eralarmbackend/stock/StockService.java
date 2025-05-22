package com.eralarm.eralarmbackend.stock;

import com.eralarm.eralarmbackend.stock.dto.StockInfoResponse;
import com.eralarm.eralarmbackend.stock.dto.StockInvalidateRequest;
import com.eralarm.eralarmbackend.stock.dto.UpdateStocksResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final StockRepository repo;
    private static final String STOCKS_URL = "https://api.nasdaq.com/api/screener/stocks";
    private static final String STOCKS_ALL_URL = "https://api.nasdaq.com/api/screener/stocks?tableonly=true&download=true";
    private final RestTemplate restTemplate;
    private final EntityManager em;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Stock getStock(String symbol) {
        return repo.findById(symbol).orElse(null);
    }

    public Page<Stock> getAllStocks(Pageable pageable) {
        return repo.findAllByEnabledTrue(pageable);
    }

    public List<String> getAllEnabledSymbols() {
        return repo.findAllByEnabledTrue().stream().map(Stock::getSymbol).toList();
    }

    public List<String> test() {
        List<String> tmp= repo.findAllByEnabledTrue().stream().map(Stock::getSymbol).toList();

        return tmp.subList(0, 10);
    }

    public JsonNode getStocksJsonNode(String url) {
        String responseBody;
        try {
            responseBody = restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            log.error("HTTP 요청 실패: {}", e.getMessage());
            return null;
        }

        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 실패: {}", e.getMessage());
            return null;
        }
    }

    @Transactional
    public UpdateStocksResponse updateStocks() {
        JsonNode check = getStocksJsonNode(STOCKS_URL);


        int totalRecords = check.path("data").path("totalrecords").asInt();

        if(totalRecords==repo.count()) {
            log.info("추가된 주식 없음");
            return new UpdateStocksResponse(0);
        }

        JsonNode stocks = getStocksJsonNode(STOCKS_ALL_URL);

        JsonNode rows = stocks.path("data").path("rows");
        if (!rows.isArray()) {
            log.warn("⚠️ 데이터가 비어 있습니다.");
            return new UpdateStocksResponse(0);
        }

        int cnt=0;
        for (JsonNode row :rows) {
            try {
                StockInfoResponse stockInfoResponse = objectMapper.treeToValue(row, StockInfoResponse.class);
                if (repo.existsById(stockInfoResponse.getSymbol().trim())) {
                    continue;
                }
                log.info("새로 추가된 주식 {}", stockInfoResponse.getSymbol());
                Stock entity = StockMapper.toEntity(stockInfoResponse);

                em.persist(entity);
                cnt++;

                if (cnt % 50 == 0) { // 배치 크기와 맞춰야 함
                    em.flush();
                    em.clear();
                }
            } catch (JsonProcessingException e) {
                log.warn("⚠️ 개별 row 파싱 실패: {}", e.getMessage());
            }
        }
        // 마지막 잔여 데이터 flush
        em.flush();
        em.clear();

        return new UpdateStocksResponse(cnt);
    }

    @Transactional
    public void invalidate(StockInvalidateRequest request) {
        log.info("주식 비활성화 : {}", request.getSymbol());
        Stock stock = repo.findById(request.getSymbol()).orElseThrow(NoSuchElementException::new);
        stock.updateEnabled(false);
    }

    public Page<Stock> findStock(String searchParam,Pageable pageable) {
        // 전체 조회를 원하는 경우 null 보다는 ""를 넘기는게 좋다
        if(searchParam == null)
            searchParam = "";

        Page<Stock> stockPage= repo.findAllBySymbolStartsWithIgnoreCaseOrNameStartsWithIgnoreCase(searchParam,searchParam,pageable);
        log.info("{}개의 검색 결과",stockPage.getNumberOfElements());
        return stockPage;
    }
}
