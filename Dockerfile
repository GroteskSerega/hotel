# Copy layers:
FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /app
COPY build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Make final image:
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S axiom && adduser -S axiom -G axiom
USER axiom:axiom

# Copy layers for cache
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

# Optimization for java 21:
ENTRYPOINT ["java", \
            "-XX:+UseG1GC", \
            "-XX:+UnlockExperimentalVMOptions", \
            "-Dspring.threads.virtual.enabled=true", \
            "org.springframework.boot.loader.launch.JarLauncher"]