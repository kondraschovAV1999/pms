# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace

# Copy Maven wrapper and configuration files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy all modules
COPY app app
COPY model model

# Ensure Unix line endings for the Maven wrapper script and run Maven install

RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests

# Stage 2: Package
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

# Copy app module dependencies
ARG DEPENDENCY=/workspace/app/target
COPY --from=build ${DEPENDENCY}/app-0.0.1-SNAPSHOT.jar /app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]