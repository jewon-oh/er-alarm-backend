package com.eralarm.eralarmbackend.earnings.message_producer;

import com.eralarm.eralarmbackend.earnings.dto.EarningsTaskSendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eralarm.eralarmbackend.rabbitmq.RabbitMqConfig.EARNINGS_EXCHANGE;
import static com.eralarm.eralarmbackend.rabbitmq.RabbitMqConfig.EARNINGS_REQUEST_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class EarningsTaskProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendToCelery(EarningsTaskSendMessage dto) {
        Map<String, Object> message = new HashMap<>();

        message.put("id", dto.getTaskId()); // Celery task_id
        message.put("task", "tasks.update_earnings"); // Celery task 이름 (Python에서 설정한 name)
        message.put("args", List.of(dto.getSymbol()));
        message.put("kwargs", Map.of());
        message.put("retries", 0);

        rabbitTemplate.convertAndSend(EARNINGS_EXCHANGE, EARNINGS_REQUEST_ROUTING_KEY, message);
    }
}
