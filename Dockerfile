
# Build stage
FROM maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /build

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create app user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/target/fraud-rule-engine-*.jar fraud-rule-engine.jar

# Change ownership to app user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "fraud-rule-engine.jar"]
