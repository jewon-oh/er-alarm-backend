<h1 style="text-align: center"> 해외 주식 실적 발표일 알림 서비스 (Backend)</h1>

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-000000?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-000000?style=for-the-badge&logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-000000?style=for-the-badge&logo=rabbitmq)
![Docker](https://img.shields.io/badge/Docker-000000?style=for-the-badge&logo=docker)

## 주요 특징

- 실적 발표일 조회 API 제공
- 사용자 맞춤형 알림 기능 (푸시 알림, 이메일 등 연동)
- 안정적인 데이터베이스 연동 및 관리
- 확장 가능한 아키텍처 설계

## 설치 및 실행

1. 저장소 클론
   ```bash
   git clone https://github.com/jewon-oh/er-alarm-backend.git
   cd er-alarm-backend
   ```
2. Docker Compose를 이용한 실행
   ```bash
   docker-compose up --build -d
   ```
   (백그라운드에서 필요한 서비스들이 실행됩니다.)

3. 서비스 중지
   ```bash
   docker-compose down
   ```