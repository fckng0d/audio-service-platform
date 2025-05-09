FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean install -pl shared-libraries/commons-grpc,shared-libraries/commons-dto -Dmaven.test.skip=true
RUN mvn clean package -pl services/s3-service -am -Dmaven.test.skip=true

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
ARG API_GATEWAY_PORT=8080
COPY --from=builder /build/services/s3-service/target/*.jar app.jar
EXPOSE ${API_GATEWAY_PORT}
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=cloudconfig"]