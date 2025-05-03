FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /build
COPY . .
RUN mvn -pl infrastructure/config-server clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG CONFIG_SERVER_PORT=8888
COPY --from=builder /build/infrastructure/config-server/target/*.jar app.jar
EXPOSE ${CONFIG_SERVER_PORT}
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=native"]