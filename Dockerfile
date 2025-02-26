FROM openjdk:17-jdk-alpine

RUN addgroup -S sedeo \
    && adduser -S sedeo -G sedeo
USER sedeo:sedeo
COPY main-web/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]