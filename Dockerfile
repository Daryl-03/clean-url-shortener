
FROM eclipse-temurin:21-jre-alpine
LABEL author="naku"
LABEL description="Dockerfile for Shortlink Application"
LABEL version="1.0"

ARG JAR_FILE=bootstrap-spring/build/libs/bootstrap-spring-*.jar

WORKDIR /app
USER nonroot

COPY ${JAR_FILE} /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]