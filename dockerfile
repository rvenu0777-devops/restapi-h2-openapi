# ====== Stage 1: Build the application ======
FROM maven:3.9.8-eclipse-temurin-17 AS build

# Set workdir inside container
WORKDIR /app

# First copy pom.xml and download dependencies (cached if pom.xml unchanged)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy source code
COPY src ./src

# Build application without running tests (speed up container build)
RUN mvn clean package -DskipTests

# ====== Stage 2: Run the application ======
FROM eclipse-temurin:17-jdk-jammy

# Set workdir inside container
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Default command
ENTRYPOINT ["java", "-jar", "app.jar"]