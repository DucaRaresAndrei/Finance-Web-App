# build
FROM gradle:8.10-jdk17-alpine AS builder
WORKDIR /app

# fisierele gradle
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle ./gradle

# sursele codului
COPY src ./src

# jar-ul SpringBoot
RUN ./gradlew bootJar --no-daemon

# runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# copiez jar-ul generat
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
