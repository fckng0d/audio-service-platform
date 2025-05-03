FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /build
COPY . .
RUN mvn -pl infrastructure/api-gateway clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG API_GATEWAY_PORT=8080
COPY --from=builder /build/infrastructure/api-gateway/target/*.jar app.jar
EXPOSE ${API_GATEWAY_PORT}
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=cloudconfig"]