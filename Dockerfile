FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

COPY gradle ./gradle
COPY gradlew .
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew

COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app
COPY --from=builder /workspace/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]