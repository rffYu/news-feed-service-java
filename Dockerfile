# Stage 1: Build the project using Maven
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy only the files needed for dependency resolution first to leverage Docker cache
COPY pom.xml .
COPY common/pom.xml ./common/pom.xml
COPY listener-service/pom.xml ./listener-service/pom.xml

COPY common/src ./common/src
COPY listener-service/src ./listener-service/src

# Build the project and package it as a jar
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the jar built in the previous stage
COPY --from=build /app/listener-service/target/*.jar app.jar

ENV SPRING_CONFIG_LOCATION=file:/app/application.yml

# Entrypoint with config path overridable via -e SPRING_CONFIG_LOCATION
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.config.location=${SPRING_CONFIG_LOCATION}"]


