# OpenTelemetry Agent 다운로드 스테이지 
FROM curlimages/curl:8.12.1 AS download
ARG OTEL_AGENT_VERSION="2.15.0"
RUN curl --silent --fail --insecure -L "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar" \
    -o "$HOME/opentelemetry-javaagent.jar"

# 빌드 스테이지
FROM gradle:8.5.0-jdk17-alpine AS build
WORKDIR /home/gradle/app

# 1. 의존성 관리를 위한 파일만 먼저 복사합니다.
COPY build.gradle settings.gradle ./

COPY gradlew .
COPY gradle ./gradle

# 2. 소스 코드를 복사하기 전에 의존성만 미리 다운로드하여 레이어에 저장합니다.
RUN gradle dependencies --quiet

# 3. 나머지 소스 코드 전체를 복사합니다.
# application.yaml 등 소스 파일이 변경되면 여기부터 캐시가 무효화되어 다시 빌드됩니다.
COPY . .
RUN gradle build -x test --quiet

# 실행 스테이지
FROM openjdk:17 AS run

COPY --from=build /home/gradle/app/build/libs/*.jar /app.jar
COPY --from=download /home/curl_user/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT ["java","-javaagent:/opentelemetry-javaagent.jar", "-jar", "/app.jar"]
