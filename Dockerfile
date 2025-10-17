# ===== Stage 1: Build =====
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build application
COPY src ./src
RUN mvn clean package -DskipTests -B

# ===== Stage 2: Runtime =====
FROM eclipse-temurin:17-jre-alpine AS runner
WORKDIR /app

# Create non-root user (UID 1001)
RUN addgroup -S spring && adduser -S spring -G spring -u 1001

# Install curl for health checks (lightweight Alpine package)
RUN apk add --no-cache curl

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar
RUN chown -R spring:spring /app

USER 1001
EXPOSE 8080

# Health check (works in Alpine)
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Build arguments and metadata
ARG JAVA_VERSION=17
ARG BUILD_DATE
ARG COMMIT_SHA

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

LABEL org.opencontainers.image.title="BTTP Backend" \
      org.opencontainers.image.description="Spring Boot Backend Application" \
      org.opencontainers.image.version="${COMMIT_SHA}" \
      org.opencontainers.image.created="${BUILD_DATE}"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
