package com.eralarm.eralarmbackend.base.rest_api;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.DefaultBackoffStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    static final int MAX_POOL = 200; // 전체 커넥션 풀에서 사용할 수 있는 최대 커넥션 수
    static final int MAX_PER_ROUTE = 100; // 각 호스트(서버)당 커넥션 풀에서 사용할 수 있는 최대 커넥션 수
    static final long VALIDATE_AFTER_INACTIVITY = 5L; // 유휴상태 검증 시간
    static final int IDLE_CONNECTION_TIMEOUT_SEC = 30; // 유휴 커넥션이 이 시간이 지나면 풀에서 제거되는 시간
    static final long REQUEST_TIME_OUT = 10000L; // 커넥션 풀에서 커넥션을 얻기 위해 대기할 수 있는 최대 시간
    static final long READ_TIME_OUT = 5000L; // 서버로부터 응답을 기다리는 최대 시간
    static final int RETRY_COUNT = 1; // 예외가 발생했을 때 요청을 재시도할 최대 횟수
    static final long BACKOFF = 1000; // 재시도 간의 대기 시간

    @Bean
    public RestTemplate restTemplate() {
        // 커넥션 관리 매니저 (커넥션 풀을 관리하는 매니저)
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(MAX_POOL);
        connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        connManager.setDefaultSocketConfig(SocketConfig.DEFAULT); // 기본 TCP 소켓 설정
        connManager.setDefaultConnectionConfig(ConnectionConfig
                .custom()
                .setValidateAfterInactivity(Timeout.ofSeconds(VALIDATE_AFTER_INACTIVITY))
                .build()); // 유휴 상태에서 커넥션을 재사용하기 전에 유효성 검사를 수행 (서버와의 연결이 끊겼는지 확인)


        // 요청 관련 다양한 설정을 제공 (타임아웃 관련 설정)
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(REQUEST_TIME_OUT))
                .setResponseTimeout(Timeout.ofMilliseconds(READ_TIME_OUT))
                .build();

        // Apache HttpClient를 사용하면 GZIP 자동 처리됨
        HttpClient httpClient= HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .setConnectionBackoffStrategy(new DefaultBackoffStrategy()) // 네트워크 오류 발생 시 재시도 간 백오프전략
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()) // 서버가 보낸 Keep-Alive 헤더 값에 따라 커넥션을 유지하는 전략
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(RETRY_COUNT, TimeValue.ofMilliseconds(BACKOFF))) // 네트워크 오류 또는 일시적인 서버 문제 발생 시 재시도 전략
                .evictIdleConnections(TimeValue.ofSeconds(IDLE_CONNECTION_TIMEOUT_SEC)) // 유휴 커넥션이 사용되지 않았을 경우 커넥션을 종료하고 풀에서 제거
                .build();


        return getRestTemplate(httpClient);
    }

    private static RestTemplate getRestTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);

        // 기본 헤더 설정을 위한 Interceptor 등록
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            // 이게 꼭 있어여 nasdaq api 요청 가능
            headers.set(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120.0.0.0 Safari/537.36");

            headers.set(HttpHeaders.CONNECTION, "keep-alive"); // TCP 연결재사용
//            headers.set(HttpHeaders.ACCEPT, "*/*");
//            headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
            return execution.execute(request, body);
        });
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
