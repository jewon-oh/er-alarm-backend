package com.eralarm.eralarmbackend.earnings;

import com.eralarm.eralarmbackend.earnings.dto.EarningsTaskSendMessage;
import com.eralarm.eralarmbackend.earnings.message_producer.EarningsTaskProducer;
import com.eralarm.eralarmbackend.earnings.service.EarningsTaskService;
import com.eralarm.eralarmbackend.earnings.util.EarningsTaskMapper;
import com.eralarm.eralarmbackend.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class EarningsUpdateScheduler {

    private final StockService stockService;
    private final EarningsTaskProducer earningsTaskProducer;
    private final EarningsTaskService earningsTaskService;

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시
    public void scheduleUpdate() {
        List<String> symbols = stockService.getAllEnabledSymbols();
//        List<String> symbols = stockService.test();
//        log.info("테스트");
        for (String symbol : symbols) {
            String taskId = UUID.randomUUID().toString();

            EarningsTaskSendMessage message =  EarningsTaskSendMessage.builder()
                    .taskId(taskId)
                    .symbol(symbol)
                    .build();

            // Task 저장
            earningsTaskService.save(EarningsTaskMapper.toSaveDto(message));

            // RabbitMQ 메시지 발행
            earningsTaskProducer.sendToCelery(message);
        }
    }
}
