FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace
COPY gradlew .
COPY gradle ./gradle
RUN chmod +x gradlew
COPY build.gradle.kts settings.gradle.kts ./
COPY . .
RUN ./gradlew clean build --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app
COPY --from=builder /workspace/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]