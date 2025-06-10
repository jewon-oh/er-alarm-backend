package com.eralarm.eralarmbackend.earnings.message_producer;


import com.eralarm.eralarmbackend.earnings.EarningsTaskStatus;
import com.eralarm.eralarmbackend.earnings.dto.EarningsTaskReceiveMessage;
import com.eralarm.eralarmbackend.earnings.dto.EarningsUpdateRequest;
import com.eralarm.eralarmbackend.earnings.service.EarningsService;
import com.eralarm.eralarmbackend.earnings.service.EarningsTaskService;
import com.eralarm.eralarmbackend.earnings.util.EarningsTaskMapper;
import com.eralarm.eralarmbackend.stock.StockService;
import com.eralarm.eralarmbackend.stock.dto.StockInvalidateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.eralarm.eralarmbackend.rabbitmq.RabbitMqConfig.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class EarningsTaskConsumer {
    private final EarningsTaskService earningsTaskService;
    private final StockService stockService;
    private final EarningsService earningsService;

    @RabbitListener(queues = EARNINGS_RESULT_QUEUE)
    public void receiveMessage(EarningsTaskReceiveMessage message)  {
        try{
            log.info("작업 {} : [{}] {}",  message.getTaskId(),
                    message.getStatus().toString(),
                    message.getMessage() == null ? "" : message.getMessage()
            );
            earningsTaskService.update(EarningsTaskMapper.toUpdateDto(message));

            if(message.getStatus().equals(EarningsTaskStatus.SUCCESS)) {
                log.info("{} : {}개 의 실적", message.getSymbol(),message.getEarningsList().size());
                EarningsUpdateRequest request = new EarningsUpdateRequest(message.getSymbol(),message.getEarningsList());
                earningsService.updateEarningsDate(request);
            }else{
                log.info("{} : {}", message.getStatus(), message.getMessage());
                StockInvalidateRequest request = new StockInvalidateRequest(message.getSymbol(),message.getMessage());
                stockService.invalidate(request);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}



