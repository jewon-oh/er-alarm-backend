FROM openjdk:17-alpine

ENV SPRING_PROFILES_ACTIVE=dev

WORKDIR /app

COPY build/libs/er-alarm-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]