@@
# Stage 1: build JAR-ника
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Сначала собираем локальный модуль events-contract,
# т.к. analytics-service зависит от org.example.events:events-contract:1.0-SNAPSHOT
COPY events-contract/pom.xml ./events-contract/
COPY events-contract/src ./events-contract/src
RUN cd events-contract && mvn clean install -DskipTests -B

# Копируем pom.xml analytics-service
COPY analytics-service/pom.xml ./analytics-service/

# Качаем зависимости (будет кешироваться, пока не меняется pom.xml)
RUN cd analytics-service && mvn dependency:go-offline -B

# Копируем исходники analytics-service и собираем JAR
COPY analytics-service/src ./analytics-service/src
RUN cd analytics-service && mvn clean package -DskipTests -B


# Stage 2: минимальный runtime-образ
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Нерутовый пользователь для безопасности
RUN addgroup -S javauser && adduser -S javauser -G javauser

# Копируем собранный JAR из builder-стейджа
COPY --from=builder /build/analytics-service/target/*.jar app.jar

RUN chown javauser:javauser /app/app.jar
USER javauser

# Порт сервиса (должен совпадать с server.port)
EXPOSE 8082

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]