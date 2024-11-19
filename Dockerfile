# Start with a base image with JDK
FROM eclipse-temurin:17

# Set the working directory
WORKDIR app/

# Copy the JAR file from host to container (optional, use volumes for dynamic updates)
COPY build/libs/cubic-server.jar app.jar

# Expose the port (Spring Boot Application Default Port)
EXPOSE 8085

# Set the default command to execute the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
