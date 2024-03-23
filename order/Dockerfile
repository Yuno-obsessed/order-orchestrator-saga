FROM ghcr.io/graalvm/jdk-community:21 AS builder

WORKDIR /app

COPY pom.xml .
COPY order/pom.xml service/pom.xml
COPY order/src service/src
COPY order/mvnw service/
COPY order/.mvn service/.mvn


RUN cd service && chmod +x ./mvnw && ./mvnw clean package -DskipTests -DskipITs

FROM ghcr.io/graalvm/jdk-community:21

WORKDIR /app

COPY --from=builder /app/service/target/order-0.0.1-SNAPSHOT.jar ./order-0.0.1-SNAPSHOT.jar

EXPOSE 8000

CMD ["java", "-jar", "order-0.0.1-SNAPSHOT.jar"]