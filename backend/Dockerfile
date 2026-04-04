# Stage 1: Build the backend application
# Using a Maven image with Eclipse Temurin Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies to utilize Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
# We skip tests here to speed up the container build process
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime environment
# We use a lighter JRE image for production to reduce image size
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the Spring Boot application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
