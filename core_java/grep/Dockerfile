# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build fat jar
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the exact JAR from builder
COPY --from=builder /app/target/JavaGrep-1.0-SNAPSHOT.jar app.jar

# Set default command
ENTRYPOINT ["java", "-jar", "app.jar"]
