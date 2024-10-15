# Use an official OpenJDK 21 runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project JAR file to the container
COPY ./target/Recommendation-System-Book-0.0.1-SNAPSHOT.jar /app/Recommendation-System-Book-0.0.1-SNAPSHOT.jar

# Expose the application port (should match your Spring Boot port)
EXPOSE 9462

# Set the environment variables for MySQL database connection
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/books_data?createDatabaseIfNotExist=true
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root

# Set the JWT environment variables
ENV APPLICATION_SECURITY_JWT_SECRET_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
ENV APPLICATION_SECURITY_JWT_EXPIRATION=8640000
ENV APPLICATION_SECURITY_JWT_REFRESH_TOKEN_EXPIRATION=60480000

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/Recommendation-System-Book-0.0.1-SNAPSHOT.jar"]
