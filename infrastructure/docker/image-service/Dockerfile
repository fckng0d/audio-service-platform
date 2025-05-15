FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean install -pl shared-libraries/commons-grpc,shared-libraries/commons-dto -Dmaven.test.skip=true
RUN mvn clean package -pl services/image-service -am -Dmaven.test.skip=true

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /build/services/image-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=cloudconfig"]