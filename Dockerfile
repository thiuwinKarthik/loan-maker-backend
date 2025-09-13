# --------------------------
# Stage 1: Build JAR with Maven
# --------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven config and source code
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests to speed up)
RUN mvn clean package -DskipTests

# --------------------------
# Stage 2: Run the JAR
# --------------------------
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose default port (for documentation; Render overrides with $PORT)
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
