FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

COPY grpc-pricing/pom.xml ./grpc-pricing/
RUN cd grpc-pricing && mvn dependency:go-offline -B

COPY grpc-pricing/src ./grpc-pricing/src
RUN cd grpc-pricing && mvn clean package -DskipTests -B


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S javauser && adduser -S javauser -G javauser

COPY --from=builder /build/grpc-pricing/target/*.jar app.jar

RUN chown javauser:javauser /app/app.jar
USER javauser

# Порт gRPC
EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]