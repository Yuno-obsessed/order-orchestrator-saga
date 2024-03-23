FROM ghcr.io/graalvm/jdk-community:21 AS builder

WORKDIR /app

COPY pom.xml .
COPY account/pom.xml service/pom.xml
COPY account/src service/src
COPY account/mvnw service/
COPY account/.mvn service/.mvn


RUN cd service && chmod +x ./mvnw && ./mvnw clean package -DskipTests -DskipITs

FROM ghcr.io/graalvm/jdk-community:21

WORKDIR /app

COPY --from=builder /app/service/target/account-0.0.1-SNAPSHOT.jar ./account-0.0.1-SNAPSHOT.jar

EXPOSE 8001

CMD ["java", "-jar", "account-0.0.1-SNAPSHOT.jar"]
