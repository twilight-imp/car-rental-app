FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# notification-service зависит от events-contract
COPY events-contract/pom.xml ./events-contract/
COPY events-contract/src ./events-contract/src
RUN cd events-contract && mvn clean install -DskipTests -B

COPY notification-service/pom.xml ./notification-service/
RUN cd notification-service && mvn dependency:go-offline -B

COPY notification-service/src ./notification-service/src
RUN cd notification-service && mvn clean package -DskipTests -B


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S javauser && adduser -S javauser -G javauser
COPY --from=builder /build/notification-service/target/*.jar app.jar
RUN chown javauser:javauser /app/app.jar
USER javauser
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]