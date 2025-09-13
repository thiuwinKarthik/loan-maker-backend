# Use official JDK image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/*.jar app.jar

# Expose default port (for documentation only, Render overrides it)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
