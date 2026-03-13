Read this in : [English](README.md) | [Русский](README.ru.md)

# Hotel service: High-Performance AOT Reference
A hotel booking microservice featuring a build-in admin panel for content management and Role-Based Access Control (RBAC). The project is optimized for Oracle GraalVM and Ahead-of-Time (AOT) compilation..

## Project Structure
```text
├── benchmarks/                    # Performance test reports & metrics
├── docker/                        # Compose files for full-stack infrastructure
├── src/main/
│   ├── java/
│   │   └── com.example.hotel
│   │       ├── aop/               # Aspects for RBAC & user operation tracking
│   │       ├── configuration/     # Admin init, AOT hints, Security, Kafka & MongoDB configs
│   │       ├── entity/            # JPA Entities (Postgres)
│   │       │   └── statistics/    # Document Entities (MongoDB)
│   │       ├── event/             # Kafka Event definitions
│   │       ├── exception/         # Custom Business Exception handling
│   │       ├── listener/          #
│   │       │   └── statistics/    # Kafka Consumers for event processing
│   │       ├── mapper/            # MapStruct interfaces (AOT-friendly)
│   │       ├── repository/        # Persistence layer for Postgres
│   │       │   └── statistics/    # Persistence layer for MongoDB
│   │       ├── security/          # Custom UserDetails & Security Principal logic
│   │       ├── service/           # Business Logic & Warmup Service
│   │       │   └── statistics/    # Analytics & Statistics processing
│   │       ├── validation/        # Custom Request Validators
│   │       └── web/               #
│   │           ├── controller/    # REST Endpoints (Admin & User APIs)
│   │           ├── dto/           # Request/Response Data Transfer Objects
│   │           └── interceptors/  # Request/Response logging & telemetry
│   └── resources/                 # application.yaml, logback-spring.xml
├── build.gradle.kts               # Build script with Bytecode Enhancement & AOT tasks
├── Dockerfile                     # Standard Layered Temurin JRE build
├── Dockerfile_axiom               # Optimized Axiom (Liberica) JRE build
├── Dockerfile_axiom_native        # Axiom NIK (musl/Alpaquita)
├── Dockerfile_axiom_native_pro    # Production-ready Native Image with advanced opts
├── Dockerfile_vanilla_native      # Oracle GraalVM Native Image
└── Dockerfile_vanilla_native_pgo  # Future: Experimental PGO (Profile Guided Optimization)

```

## Performance Benchmarks (JRE vs Native)
![](images/-90percents.PNG)

### Hardware Setup
The benchmarks were conducted on an **HP EliteBook 8770w** workstation:
- **CPU:** Intel i7-3720QM (4 Cores / 8 Threads)
- **RAM:** 32.0 GB
- **Storage:** SSD
- **OS:** Windows 10
- **Containerization:** Docker Desktop

### The "Hotel Service" Stack

- **Java 21 + Project Loom (Virtual Threads):** Leveraging Virtual Threads for high concurrency.
- **Spring Boot 3.5 + Hibernate 6.6:** Utilizing AOT (Ahead-of-Time) capabilities and Bytecode Enhancement.
- **Infrastructure:** PostgreSQL 17, MongoDB 7, and Apache Kafka 3.x

### The Battle in Numbers: JRE vs Native Image

| Metric                    | Eclipse Temurin (JRE) | Oracle GraalVM (Native Image) | Win %          |
|:--------------------------|:----------------------|:------------------------------|:---------------|
| **Memory (RSS)**          | 386.8 MiB             | **38.0 MiB**                  | **-90.1%**     |
| **Startup Time**          | 22.542 sec            | **1.27 sec**                  | **18x Faster** |
| **OS Threads (PIDS)**     | 51                    | **28**                        | **-45%**       |
| **First Request Latency** | ~1176 ms              | **912 ms**                    | **-23%**       |

> **Key takeaway**: Optimization for Oracle GraalVM allows running up to 10 service instances on the same hardware resources previously required for a single one.

## Core Features
- **RBAC Model**: Pre-configured ADMIN role for system initialization; support for both ADMIN and USER roles.
- **Async Engine**: Spring MVC-based stack enhanced by Project Loom.
- **Cloud Native**: Fully compatible with native compilation and optimized for Read-Only file systems.
- **AOT-friendly Architecture**: Leverages MapStruct and Static Metamodel while avoiding dynamic proxies.
- **Data Integrity**: Uses `TransactionSynchronizationManager` to ensure Kafka events are dispatched only after a successful Postgres commit.
- **High-Density**:  Optimized Hibernate queries using `@EntityGraph` and `Slice` to minimize Heap memory usage.
- **High-Perf JPA**: Implementation includes UUID v7, Specification API, Slice and `@EntityGraph`.
- **Native Warmup Strategy**: Custom `WarmupService` triggered by `ApplicationReadyEvent` to pre-warm the network stack and connection pools, eliminating Cold Start issues.
- **Zero-Overhead Observability**: Prometheus and Actuator integration specifically tailored for Native Image (Zero Classloading Reflection).
- **Infrastructure-as-Code Friendly**: Full support for health and readiness probes, enabling instant autoscaling in Kubernetes/Knative environments.
- **Grafana**: Service monitoring is available at http://localhost:3000/. Pre-configured Dashboard ID: 11378.

## Optimization Details
- **Static Indexing**: Utilizes `spring-context-indexer` to eliminate runtime classpath scanning.
- **Hibernate Enhancement**: Build-time bytecode instrumentation to support Lazy Loading without dynamic proxies.
- **Runtime Hints**: Custom hint registration for complex DTO serialization and database driver compatibility in AOT mode.
- **Memory Management**: Strict heap limitation (`-Xmx64m`) while maintaining high throughput thanks to Project Loom.

## Observability & Monitoring
The service provides a comprehensive telemetry suite:

- **Health Check**: `GET /actuator/health` (Liveness & Readiness probes).
- **Prometheus Metrics**: `GET /actuator/prometheus` (JVM, CPU, Connections).
- **Deep Metrics**: `GET /actuator/metrics` (accessible for ADMIN).

For monitoring the native image, specific `native_image_memory_used_bytes`metrics are used, allowing for byte-perfect tracking of RAM consumption.

## Build & Deployment
The project supports the standard Gradle build lifecycle and containerization:

```bash
# Build the artifact
./gradlew bootJar
```

```bash
docker build -t hotel-app .
```
## Environment Variables

| Переменная             | Описание                      | Дефолтное значение |
|------------------------|-------------------------------|--------------------|
| APP_SERVER_PORT        | Service port                  | 8080               |
| APP_ADMIN_USERNAME     | System administrator username | admin              |          
| APP_ADMIN_PASSWORD     | System administrator password | admin              |
| DB_HOST                | Postgres connection host      | localhost          |
| DB_PORT                | Postgres connection port      | 5432               |
| DB_NAME                | Postgres database name        | hotel_db           |
| DB_SCHEMA              | Postgres schema name          | hotel_schema       |
| DB_USER                | Postgres username             | postgres           |
| DB_PASSWORD            | Postgres password             | postgres           |
| MONGODB_HOST           | MongoDB connection host       | localhost          |
| MONGODB_PORT           | MongoDB connection port       | 27017              |
| MONGODB_DBNAME         | MongoDB database name         | statistics-db      |
| MONGODB_USER           | MongoDB username              | root               |
| MONGODB_PASSWORD       | MongoDB password              | root               |
| KAFKA_HOST             | Kafka connection host         | localhost          |
| KAFKA_PORT             | Kafka connection port         | 9092               |
| SPRING_PROFILES_ACTIVE | Active profile (native/pgo)   | default            |


## Deployment Options

The application is designed for various environments, ranging from local development to high-density production clusters.

### 1. Bare Metal / Local JRE
To run the standard executable JAR file:
```bash
java -jar hotel.jar --spring.config.import=optional:file:./application.yml
```

### 2. Docker (Standalone)
```bash
docker run --rm -p 8080:8080 \
  --name hotel \
  -e APP_SERVER_PORT=8080 \
  -e APP_ADMIN_USERNAME=admin \
  -e APP_ADMIN_PASSWORD=admin \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=hotel_db \
  -e DB_SCHEMA=hotel_schema \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  -e MONGODB_HOST=host.docker.internal \
  -e MONGODB_PORT=27017 \
  -e MONGODB_DBNAME=statistics-db \
  -e MONGODB_USER=root \
  -e MONGODB_PASSWORD=root \
  -e KAFKA_HOST=host.docker.internal \
  -e KAFKA_PORT=9092 \
  hotel
```

### 3. Docker Compose (Full Stack)
```bash
# Deploy the stack in background mode
# Starts the complete stack (App + Postgres + Mongo + Kafka)
docker-compose up -d

# Monitor service logs
docker-compose logs -f app
```

### 4. Native Image Execution (Oracle GraalVM)
```bash
./hotel -Xmx64m -Xms64m
```

# Getting Started
## Local JRE Execution
To run the application on your local machine:
```
java -jar -Dspring.config.location=application.yaml hotel-0.0.1-SNAPSHOT.jar
```

## Docker Container
To run the application as a standalone Docker container:
```
docker run --rm \
  -e APP_ADMIN_USERNAME=admin \
  -e APP_ADMIN_PASSWORD=admin \
  -e DB_HOST=localhost \
  -e DB_PORT=5436 \
  -e DB_NAME=hotel_db \
  -e DB_SCHEMA=hotel_schema \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  -e APP_SERVER_PORT=8080 \
  -e MONGODB_HOST=host.docker.internal \
  -e MONGODB_PORT=27017 \
  -e MONGODB_DBNAME=statistics-db \
  -e MONGODB_USER=root \
  -e MONGODB_PASSWORD=root \
  -e KAFKA_HOST=localhost \
  -e KAFKA_PORT=9092 hotel
```

## Docker Compose
To launch the entire infrastructure (including databases and message broker):
```
cd docker
docker-compose up
```

To stop and remove containers:
```
docker-compose down
```

# Advanced Topics (Research)
## Building with Different Docker Images

### 1. Layered Build (Eclipse Temurin)
An optimized layered build approach.
**Benefit**: Separating dependencies from the application code allows for efficient Docker layer caching, significantly reducing network traffic during CI/CD pipelines.

```
docker build -t hotel:layers -t hotel:hotel-layers .
```

### 2. Uber-JAR (Legacy)
A traditional "all-in-one" build approach.
**Deprecated** (2026): Results in excessive data transfer overhead even for minimal code changes.
```
docker build -f Dockerfile_uber_jar -t hotel:hotel-uber .
```

### 3. Axiom (Alpaquita + Musl)
An optimized layered build based on the Axiom image. Separating dependencies from application code enables efficient Docker layer caching, minimizing CI/CD network traffic.
- **Base Image**: `bellsoft/liberica-runtime-alpine` (or Alpaquita).
- **Benefit**: A perfect balance between build speed and caching efficiency.
```
docker build -f Dockerfile_axiom -t hotel:hotel-axiom .
```

### 4. Axiom (musl) Native Image Kit (NIK)
Compiling into a native binary using **musl libc**.
- **Build Metrics**: ~13.3 min (на i7-3720QM). Requires 8GB+ RAM for the static object grapgh analysis phase.
- **Startup**: **1.566s** (Ready to serve).
- **Security**: Runs within the **Alpaquita Cloud Native OS** environment as a non-privileged (non-root) user.
- **Verdict**: ideal for scalable microservices and Serverless architectures.
```
docker build -f Dockerfile_axiom_native -t hotel:hotel-axiom-native .
docker build -f Dockerfile_axiom_native -t hotel:hotel-native .
```

### 5. Vanilla (debian:bookworm-slim) Native
A reference GraalVM build based on GLIBC (Debian Slim).
- **Build Metrics**: ~20.6 min (на i7-3720QM) (в 1.8x slower than Axiom). Requires 8GB+ RAM for the static object graph analysis phase.
- **Startup**: **1,276s** (Aggressive optimization at the cost of build time).
- **Issues**: Network instability during the build phase and increased final image size due to heavier Debian layers.
```
docker build -f Dockerfile_vanilla_native -t hotel:hotel-vanilla-native .
```

### 6. Vanilla (debian:bookworm-slim) Native with PGO
Future Work.
- **Status**: Research in progress.
- **Target**: implementing **PGO (Profile Guided Optimization)** to push memory limits below **40 MB** and reduce response times by 15-20%.
```
docker build -f Dockerfile_vanilla_native_pgo -t hotel:hotel-vanilla-native-pgo .
```

## Summary Results
### Comparison of Build Variants and Docker Statistics.
The table below displays the resulting image sizes and build performance.
- Image Sizes:

| REPOSITORY | TAG                      | IMAGE ID      | BUILDING TIME (s) | SIZE  |
|------------|--------------------------|---------------|-------------------|-------|
| hotel      | hotel-layers             | ae26d29918f9  | 7.3               | 460MB |
| hotel      | hotel-uber               | 4cabf402d48b  | 8.5               | 456MB |
| hotel      | hotel-axiom              | af088597f55a  | 12.8              | 362MB |
| hotel      | hotel-axiom-native       | bd2e31f8d511  | 800.1             | 317MB |
| hotel      | hotel-vanilla-native     | abd85388adfc  | 1237.8            | 443MB |
| hotel      | hotel-vanilla-native-pgo | -             | -                 | -     |

- Docker Stats (Initial Run):

Pay close attention to MEM USAGE and PIDS (number of system threads). 
1st Run:

| CONTAINER ID | NAME                     | CPU % | MEM USAGE / LIMIT   | MEM % | NET I/O         | BLOCK I/O       | PIDS |
|--------------|--------------------------|-------|---------------------|-------|-----------------|-----------------|------|
| 68dad70a5e17 | hotel-layers             | 3.24% | 323.4MiB / 15.58GiB | 2.03% | 25.8kB / 26kB   | 8.19kB / 61.4kB | 55   |
| bd3f0ffd16ef | hotel-uber               | 2.94% | 350.6MiB / 15.58GiB | 2.20% | 24.2kB / 25.4kB | 0B / 49.2kB     | 53   |
| c209959f86a4 | hotel-axiom              | 0.29% | 399.4MiB / 15.58GiB | 2.50% | 24.1kB / 25.3kB | 0B / 49.2kB     | 55   |
| a1c22e9f5892 | hotel-axiom-native       | 0.05% | 49.08MiB / 15.58GiB | 0.31% | 22.7kB / 24.2kB | 0B / 0B         | 26   |
| 3e62c302c13d | hotel-vanilla-native     | 0.03% | 34.54MiB / 15.58GiB | 0.22% | 26.5kB / 25.7kB | 0B / 0B         | 25   |
| -            | hotel-vanilla-native-pgo | -     | -                   | -     | -               | -               | -    |

- Startup Time and Request/Response Latency (Initial Run):

| CONTAINER ID | NAME                     | Started(s) | Ready to Serve(s) | 1st Request Latency (ms) | 2nd Request Latency (ms) |
|--------------|--------------------------|------------|-------------------|--------------------------|--------------------------|
| 68dad70a5e17 | hotel-layers             | 15.524     | 16.461            | 1176                     | 404                      |
| bd3f0ffd16ef | hotel-uber               | 16.81      | 18.648            | 1019                     | 381                      |
| c209959f86a4 | hotel-axiom              | 13.8       | 14.763            | 1035                     | 403                      |
| a1c22e9f5892 | hotel-axiom-native       | 1.566      | 1.592             | 1255                     | 388                      |
| 3e62c302c13d | hotel-vanilla-native     | 1.357      | 1.377             | 912                      | 436                      |
| -            | hotel-vanilla-native-pgo | -          | -                 | -                        | -                        |

- Docker stats (2nd Run):

Pay close attention to MEM USAGE and PIDS (total number of system threads). 
2nd Run:

| CONTAINER ID | NAME                     | CPU % | MEM USAGE / LIMIT   | MEM % | NET I/O         | BLOCK I/O   | PIDS |
|--------------|--------------------------|-------|---------------------|-------|-----------------|-------------|------|
| 68dad70a5e17 | hotel-layers             | 0.39% | 327.9MiB / 15.58GiB | 2.05% | 36.4kB / 39.1kB | 0B / 32.8kB | 55   |
| bd3f0ffd16ef | hotel-uber               | 3.36% | 346.8MiB / 15.58GiB | 2.17% | 37.9kB / 39.5kB | 0B / 57.3kB | 53   |
| c209959f86a4 | hotel-axiom              | 0.28% | 395.1MiB / 15.58GiB | 2.48% | 37.9kB / 39.5kB | 0B / 49.2kB | 54   |
| a1c22e9f5892 | hotel-axiom-native       | 0.12% | 49.61MiB / 15.58GiB | 0.31% | 36.4kB / 38.6kB | 0B / 0B     | 26   |
| 3e62c302c13d | hotel-vanilla-native     | 2.80% | 36.06MiB / 15.58GiB | 0.23% | 36.4kB / 38.7kB | 0B / 0B     | 26   |
| -            | hotel-vanilla-native-pgo | -     | -                   | -     | -               | -           | -    |

- Startup Time and Request/Response Latency (2nd Run):

| CONTAINER ID | NAME                     | Started(s) | Ready to Serve(s) | 1st Request Latency (ms) | 2nd Request Latency (ms) |
|--------------|--------------------------|------------|-------------------|--------------------------|--------------------------|
| 68dad70a5e17 | hotel-layers             | 15.227     | 15.853            | 1121                     | 411                      |
| bd3f0ffd16ef | hotel-uber               | 16.628     | 17.996            | 1212                     | 453                      |
| c209959f86a4 | hotel-axiom              | 14.411     | 15.09             | 1141                     | 451                      |
| a1c22e9f5892 | hotel-axiom-native       | 1.458      | 1.472             | 879                      | 435                      |
| 3e62c302c13d | hotel-vanilla-native     | 1.276      | 1.286             | 864                      | 451                      |
| -            | hotel-vanilla-native-pgo | -          | -                 | -                        | -                        |


**Summary**: It is important to highlight that **JRE-based images** required **13.5-18.5 seconds** to start, whereas **Native Images** achieved startup times of just **1.2-1.5 seconds**.

### Summary Comparison Table
JRE (Axiom) vs JRE (Temurin) vs Axiom NIK (Pro) vs Vanilla Native

**Environment Configuration**:

- **Stack**: Java 21, Spring Boot 3.5.x, Hibernate 6.6 (Bytecode Enhancement), Project Loom, Postgres 17, MongoDB 7, Kafka 6.
- **Hardware**: Intel i7-3720QM (4 Cores / 8 Threads), 32.0 GB RAM, Storage SSD
- **OS**: Windows 10, Docker Desktop

| Metric          | JRE (Axiom/Layers) | JRE (Temurin/Layers) | Axiom NIK Native     | Vanilla Native (Oracle) | Best Result             |
|-----------------|--------------------|----------------------|----------------------|-------------------------|-------------------------|
| Build Time      | 12.8 sec           | 7.3 sec              | 13.3 min             | 20.6 min                | JRE (Instant)           |
| Image Size      | 362 MiB            | 460 MiB              | **317 MiB**          | 443 MiB                 | Axiom NIK (-31%)        |
| Startup Time    | 16.03 sec          | 22.54 sec            | 1.458 sec            | **1.276 sec**           | Vanilla (Record)        |
| RAM (Idle)      | 427.1 MiB          | 386.0 MiB            | 49.6 MiB             | **38.1 MiB**            | Vanilla (-90%)          |
| Threads (PIDS)  | 55                 | 55                   | **26**               | **26**                  | Native (в 2 раза легче) |
| 1st Req Latency | 1141 ms            | 1121 ms              | 879 ms               | **864 ms**              | Vanilla                 |
| OS (Runtime)	   | BellSoft / Alpine  | Debian-based         | **Alpaquita (musl)** | Debian-slim             | Alpaquita (Ultra-light) |

### Key Takeaways
- **Extreme Density**: Transitioning to Native Image reduced RAM consumption from **386-427** MB down to **36-49 MB**. This represents a **10x improvement** in service density. A single standard node can now host up to 10 Hotel service instances instead of one.
- **Project Loom Efficiency**: The synergy of Virtual Threads (Loom) and the native runtime reduced system thread counts (PIDs) by more than **50%** (from 55 to 26). This minimizes OS context-switching overhead and makes the system resilient to traffic spikes.
- **Hibernate AOT Optimization**: Leveraging Bytecode Enhancement alongside the GraalVM Metadata Repository allowed full retention of Lazy Loading functionality within the native binary, eliminating common proxy initialization errors typical for Native mode.
- **Infrastructure Leader**: Axiom NIK (Alpaquita/musl) proved its superiority in artifact compactness, saving **126 MB** of disk space compared to Vanilla (Debian/glibc). Furthermore, Axiom builds **35% faster** than its competitor, which is critical for CI/CD pipeline velocity..

### Economic Impact
- **TCO (Total Cost of Ownership)**: A 90% reduction in RAM requirements enables a radical decrese in cloud infrastructure costs (AWS/Azure/Yandex Cloud).
- **Scale-to-Zero Ready**: Despite a heavy relational stack, startup time was slashed from 13 seconds to ~1 second. This makes the service a viable candidate for Serverless architectures.
- **Architectural Evolution**: During R&D, an architectural decision was made to move migrations (Liquibase) to external init-containers. This ensured AOT build stability and predictable warmup times.