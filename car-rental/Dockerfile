FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# car-rental зависит от car-rental-api и events-contract,
# поэтому сначала собираем эти модули
COPY car-rental-api/pom.xml ./car-rental-api/
COPY car-rental-api/src ./car-rental-api/src
RUN cd car-rental-api && mvn clean install -DskipTests -B

COPY events-contract/pom.xml ./events-contract/
COPY events-contract/src ./events-contract/src
RUN cd events-contract && mvn clean install -DskipTests -B

# Подготавливаем car-rental
COPY car-rental/pom.xml ./car-rental/
RUN cd car-rental && mvn dependency:go-offline -B

COPY car-rental/src ./car-rental/src
RUN cd car-rental && mvn clean package -DskipTests -B


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S javauser && adduser -S javauser -G javauser

COPY --from=builder /build/car-rental/target/*.jar app.jar
RUN chown javauser:javauser /app/app.jar
USER javauser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]