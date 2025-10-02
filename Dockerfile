
FROM eclipse-temurin:21-jre-alpine
LABEL author="naku"
LABEL description="Dockerfile for Shortlink Application"

ARG JAR_FILE=bootstrap-spring/build/libs/bootstrap-spring-*.jar

WORKDIR /app

COPY ${JAR_FILE} /app/app.jar

RUN chmod 755 /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]