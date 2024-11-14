# Start with a base image with JDK
FROM openjdk:17-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY /build/libs/cubic-server.jar app.jar

# Expose the port (8080을 Spring Boot 애플리케이션의 기본 포트로 설정)
EXPOSE 80

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]