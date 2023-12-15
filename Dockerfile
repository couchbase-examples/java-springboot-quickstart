# # # Stage 1: Build Couchbase Server
# # FROM couchbase AS couchbase-build
# # RUN chmod +x /entrypoint.sh
# # COPY configure-server.sh /opt/couchbase
# # RUN chmod +x /opt/couchbase/configure-server.sh

# # Stage 2: Build Java Spring Boot Application
# FROM eclipse-temurin:17-jdk-jammy AS java-app-build
# ENV HOME=/usr/app
# RUN mkdir -p "$HOME"
# WORKDIR "$HOME"
# COPY . "$HOME"
# RUN --mount=type=cache,target=/root/.m2 ./mvnw -f "$HOME"/pom.xml clean package -DskipTests=true

# # Stage 3: Final Image
# FROM eclipse-temurin:17-jre-jammy 

# # Copy artifacts from the Couchbase build stage
# COPY --from=couchbase-build /opt/couchbase /opt/couchbase

# # Copy artifacts from the Java Spring Boot application build stage
# ARG JAR_FILE=/usr/app/target/*.jar
# COPY --from=java-app-build $JAR_FILE /app/runner.jar

# # Expose ports
# EXPOSE 8080
# EXPOSE 8091
# EXPOSE 8092
# EXPOSE 8093
# EXPOSE 8094
# EXPOSE 8095
# EXPOSE 8096
# EXPOSE 11207
# EXPOSE 11210
# EXPOSE 11211
# EXPOSE 18091
# EXPOSE 18092
# EXPOSE 18093
# EXPOSE 18094
# EXPOSE 18095
# EXPOSE 18096

# # Entrypoint
# ENTRYPOINT java -jar /app/runner.jar


FROM eclipse-temurin:17-jdk-jammy AS build
ENV HOME=/usr/app
RUN mkdir -p "$HOME"
WORKDIR "$HOME"
COPY . "$HOME"
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f "$HOME"/pom.xml clean package -DskipTests=true

#
# Package stage
#
FROM eclipse-temurin:17-jre-jammy 
ARG JAR_FILE=/usr/app/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/runner.jar