
# Define the base image for the build stage
FROM eclipse-temurin:17-jdk-jammy AS build

# Set the environment variable for the application home directory
ENV HOME=/usr/app

# Create the application home directory
RUN mkdir -p "$HOME"

# Set the working directory to the application home directory
WORKDIR "$HOME"

# Copy the entire project to the application home directory
COPY . "$HOME"

# Build the project using Maven, skipping tests
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f "$HOME"/pom.xml clean package -DskipTests=true

# Define the base image for the final stage
FROM eclipse-temurin:17-jre-jammy 

# Set the argument for the JAR file location
ARG JAR_FILE=/usr/app/target/*.jar

# Copy the JAR file from the build stage to the final stage
COPY --from=build $JAR_FILE /app/runner.jar

# Expose port 8080 for the application
EXPOSE 8080

# Set the entrypoint command to run the JAR file
ENTRYPOINT java -jar /app/runner.jar

# This Dockerfile is used to build a Docker image for a Java Spring Boot application.
# To build the image, run:
#  docker build -t java-springboot-quickstart .
# To run the container, use:
#  docker run -d --name springboot-container -p 9440:8080 java-springboot-quickstart -e DB_CONN_STR=<connection_string> -e DB_USERNAME=<username> -e DB_PASSWORD=<password>
