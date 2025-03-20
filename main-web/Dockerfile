FROM eclipse-temurin:17-jdk-alpine

RUN addgroup -S sedeo && adduser -S sedeo -G sedeo
USER sedeo:sedeo

WORKDIR /app

ARG JAR_FILE
ADD target/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]