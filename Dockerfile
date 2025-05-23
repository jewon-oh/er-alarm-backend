FROM curlimages/curl:8.12.1 AS download
ARG OTEL_AGENT_VERSION="2.15.0"
RUN curl --silent --fail --insecure -L "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar" \
    -o "$HOME/opentelemetry-javaagent.jar"

FROM gradle:8.5.0-jdk17-alpine AS build
WORKDIR /home/gradle/app
COPY . .
RUN gradle clean build -x test --quiet

FROM openjdk:17 AS run

COPY --from=build /home/gradle/app/build/libs/er-alarm-backend-0.0.1-SNAPSHOT.jar /app.jar
COPY --from=download /home/curl_user/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ENTRYPOINT ["java","-javaagent:/opentelemetry-javaagent.jar", "-jar", "/app.jar"]
