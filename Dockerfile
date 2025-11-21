FROM gradle:8.8-jdk21-alpine AS builder
LABEL author="naku"
LABEL description="Build stage for Hopper backend Application "

WORKDIR /home/gradle/src

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle

COPY . .

RUN ./gradlew clean :bootstrap-spring:bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine
LABEL author="naku"
LABEL description="Final stage for Hopper backend Application "


RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=builder /home/gradle/src/bootstrap-spring/build/libs/bootstrap-spring-*.jar /app/app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]