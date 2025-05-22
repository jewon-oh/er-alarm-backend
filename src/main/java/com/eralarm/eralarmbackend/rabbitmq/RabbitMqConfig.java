package com.eralarm.eralarmbackend.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitmqPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;

    // FCM 알림용
    public static final String FCM_EXCHANGE = "fcm.exchange";
    public static final String FCM_QUEUE = "fcm.queue";
    public static final String FCM_ROUTING_KEY = "fcm.send";

    // 실적 업데이트용
    public static final String EARNINGS_EXCHANGE = "earnings.update";
    public static final String EARNINGS_REQUEST_QUEUE = "earnings.request_queue";
    public static final String EARNINGS_RESULT_QUEUE = "earnings.result_queue";
    public static final String EARNINGS_REQUEST_ROUTING_KEY = "earnings.request";
    public static final String EARNINGS_RESULT_ROUTING_KEY = "earnings.result";

    // FCM 설정
    @Bean
    public Queue fcmQueue() {
        return new Queue(FCM_QUEUE);
    }

    @Bean
    public DirectExchange fcmExchange() {
        return new DirectExchange(FCM_EXCHANGE);
    }

    @Bean
    public Binding fcmBinding() {
        return BindingBuilder.bind(fcmQueue())
                .to(fcmExchange())
                .with(FCM_ROUTING_KEY);
    }
    @Bean
    public Queue earningsRequestQueue() {
        return new Queue(EARNINGS_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue earningsResultQueue() {
        return new Queue(EARNINGS_RESULT_QUEUE, true);
    }

    @Bean
    public DirectExchange earningsExchange() {
        return new DirectExchange(EARNINGS_EXCHANGE);
    }

    @Bean
    public Binding earningsReQuestBinding() {
        return BindingBuilder.bind(earningsRequestQueue())
                .to(earningsExchange())
                .with(EARNINGS_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding earningsResultBinding() {
        return BindingBuilder.bind(earningsResultQueue())
                .to(earningsExchange())
                .with(EARNINGS_RESULT_ROUTING_KEY);
    }
    /**
     * RabbitMQ 연결을 위한 ConnectionFactory 빈을 생성하여 반환
     *
     * @return ConnectionFactory 객체
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitmqHost);
        connectionFactory.setPort(rabbitmqPort);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory.getRabbitConnectionFactory();
    }

    /**
     * RabbitTemplate을 생성하여 반환
     *
     * @param connectionFactory RabbitMQ와의 연결을 위한 ConnectionFactory 객체
     * @return RabbitTemplate 객체
     */
    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // JSON 형식의 메시지를 직렬화하고 역직렬할 수 있도록 설정
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Jackson 라이브러리를 사용하여 메시지를 JSON 형식으로 변환하는 MessageConverter 빈을 생성
     *
     * @return MessageConverter 객체
     */
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
