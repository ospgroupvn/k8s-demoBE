# ===== Stage 1: Build =====
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ===== Stage 2: Runtime =====
FROM openjdk:17-jre-slim AS runner

WORKDIR /app

# Create non-root user
RUN groupadd --system --gid 1001 spring && \
    useradd --system --uid 1001 --gid spring spring

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set correct permissions
RUN chown -R spring:spring /app
USER spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Build arguments
ARG JAVA_VERSION=17
ARG BUILD_DATE
ARG COMMIT_SHA

# Environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

# Labels
LABEL org.opencontainers.image.title="BTTP Backend"
LABEL org.opencontainers.image.description="Spring Boot Backend Application"
LABEL org.opencontainers.image.version="${COMMIT_SHA}"
LABEL org.opencontainers.image.created="${BUILD_DATE}"

# Start the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]