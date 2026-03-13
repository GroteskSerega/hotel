Читать: [English](README.md) | [Русский](README.ru.md)

# Hotel service: High-Performance AOT Reference
Микросервис бронирования отелей с возможностью управлять контентом через административную панель с ролевой моделью доступа (RBAC), оптимизированный для запуска в среде **Oracle GraalVM**.

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
Исследование проведено на оборудовании **HP EliteBook 8770w** workstation:
- **CPU:** Intel i7-3720QM (4 Cores / 8 Threads)
- **RAM:** 32.0 GB
- **Storage:** SSD
- **OS:** Windows 10
- **Containerization:** Docker Desktop

### The "Hotel Service" Stack

- **Java 21 + Project Loom (Virtual Threads):** использование Virtual Threads для обеспечения высокой параллельности.
- **Spring Boot 3.5 + Hibernate 6.6:** Использование возможностей AOT (Ahead-of-Time) и улучшения Bytecode.
- **Infrastructure:** PostgreSQL 17, MongoDB 7, and Apache Kafka 3.x

### The Battle in Numbers: JRE vs Native Image

| Метрика                   | Eclipse Temurin (JRE) | Oracle GraalVM (Native Image) | Win %          |
|:--------------------------|:----------------------|:------------------------------|:---------------|
| **Memory (RSS)**          | 386.8 MiB             | **38.0 MiB**                  | **-90.1%**     |
| **Startup Time**          | 22.542 sec            | **1.27 sec**                  | **X18 Faster** |
| **OS Threads (PIDS)**     | 51                    | **28**                        | **-45%**       |
| **First Request Latency** | ~1176 ms              | **912 ms**                    | **-23%**       |

> **Key takeaway**: Оптимизация под Oracle позволяет запускать до 10 инстансов сервиса на тех же ресурсах, где раньше помещался один.

## Core Features
- **RBAC Model**: Предустановленный ADMIN для инициализации системы, поддержка ролей ADMIN и USER.
- **Async Engine**: Стек на базе Spring MVC с поддержкой Project Loom.
- **Cloud Native**: Полная готовность к нативной компиляции и работе в Read-Only файловых системах.
- **AOT-friendly Architecture**: Использование MapStruct, Static Metamodel и отказ от динамических прокси.
- **Data Integrity**: Использование TransactionSynchronizationManager для гарантии доставки событий в Kafka только после коммита в Postgres.
- **High-Density**:  Оптимизация Hibernate-запросов через @EntityGraph и Slice для минимизации Heap-памяти.
- **High-Perf JPA**: UUID v7, Specification API, Slice и @EntityGraph.
- **Native Warmup Strategy**: Кастомный WarmupService для прогрева сетевого стека и пулов соединений через ApplicationReadyEvent, устраняющий проблему Cold Start.
- **Zero-Overhead Observability**: Интеграция с Prometheus и Actuator, адаптированная под Native Image (Zero Classloading Reflection).
- **Infrastructure-as-Code Friendly**: Полная поддержка health и readiness проб для мгновенного автоскейлинга в Kubernetes/Knative.
- **Grafana**: Мониторинг сервиса доступен по адресу http://localhost:3000/. ID готового Dashboard 11378.

## Optimization Details
- **Static Indexing**: Использование `spring-context-indexer` для исключения сканирования classpath в рантайме.
- **Hibernate Enhancement**: Инструментация байт-кода на этапе сборки для поддержки Lazy Loading без динамических прокси.
- **Runtime Hints**: Регистрация кастомных хинтов для сериализации сложных DTO и работы драйверов БД в AOT-режиме.
- **Memory Management**: Жесткое ограничение кучи (`-Xmx64m`) при сохранении высокой пропускной способности за счет Project Loom.

## Observability & Monitoring
Сервис предоставляет полный набор инструментов для телеметрии:

- **Health Check**: `GET /actuator/health` (Liveness & Readiness probes).
- **Prometheus Metrics**: `GET /actuator/prometheus` (JVM, CPU, Connections).
- **Deep Metrics**: `GET /actuator/metrics` (доступно для ADMIN).

Для мониторинга нативного образа используются специфичные метрики `native_image_memory_used_bytes`, позволяющие отслеживать потребление RAM с точностью до байта.

## Build & Deployment
Проект поддерживает стандартный цикл сборки Gradle и контейнеризацию:

```bash
# Сборка артефакта
./gradlew bootJar
```

```bash
docker build -t hotel-app .
```
## Environment Variables

| Переменная             | Описание                         | Дефолтное значение |
|------------------------|----------------------------------|--------------------|
| APP_SERVER_PORT        | Порт публикации сервиса          | 8080               |
| APP_ADMIN_USERNAME     | Логин системного администратора  | admin              |          
| APP_ADMIN_PASSWORD     | Пароль системного администратора | admin              |
| DB_HOST                | Host подключения к Postgres      | localhost          |
| DB_PORT                | Port подключения к Postgres      | 5432               |
| DB_NAME                | База данных в Postgres           | hotel_db           |
| DB_SCHEMA              | Схема в Postgres                 | hotel_schema       |
| DB_USER                | Логин в Postgres                 | postgres           |
| DB_PASSWORD            | Пароль в Postgres                | postgres           |
| MONGODB_HOST           | Host подключения к MongoDB       | localhost          |
| MONGODB_PORT           | Port подключения к MongoDb       | 27017              |
| MONGODB_DBNAME         | База данных в MongoDB            | statistics-db      |
| MONGODB_USER           | Логин в MongoDB                  | root               |
| MONGODB_PASSWORD       | Пароль в MongoDB                 | root               |
| KAFKA_HOST             | Host подключения к Kafka         | localhost          |
| KAFKA_PORT             | Port подключения к Kafka         | 9092               |
| SPRING_PROFILES_ACTIVE | Профиль запуска (native/pgo)     | default            |


## Deployment Options

Приложение подготовлено к работе в различных окружениях: от локальной разработки до высокоплотных кластеров.

### 1. Bare Metal / Local JRE
Для запуска классического JAR-файла:
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
# Развертывание стека в фоновом режиме
# Запуск всего стека (App + Postgres + Mongo + Kafka)
docker-compose up -d

# Мониторинг логов
docker-compose logs -f app
```

### 4. Native Image Execution (Oracle GraalVM)
```bash
./hotel -Xmx64m -Xms64m
```

# Запуск проекта
- Запуск приложения на локальной машине:
```
java -jar -Dspring.config.location=application.yaml hotel-0.0.1-SNAPSHOT.jar
```
- Запуск приложения в Docker контейнере:
```
docker run --rm -e APP_ADMIN_USERNAME=admin -e APP_ADMIN_PASSWORD=admin -e DB_HOST=localhost -e DB_PORT=5436 -e DB_NAME=hotel_db -e DB_SCHEMA=hotel_schema -e DB_USER=postgres -e DB_PASSWORD=postgres -e APP_SERVER_PORT=8080 -e MONGODB_HOST=host.docker.internal -e MONGODB_PORT=27017 -e MONGODB_DBNAME=statistics-db -e MONGODB_USER=root -e MONGODB_PASSWORD=root -e KAFKA_HOST=localhost -e KAFKA_PORT=9092 hotel
```
- Запуск приложения с помощью Docker-compose:
```
cd docker
docker-compose up
```
- Остановка приложения с помощью Docker-compose:
```
docker-compose down
```

# Продвинутый блок (исследования)
## Сборка проекта с разными Docker-образами

### 1. Слоеная сборка (Temurin)
Оптимизированная слоеная сборка.
**Benefit**: Разделение зависимостей и прикладного кода позволяет эффективно использовать Docker Cache, минимизируя трафик при CI/CD.

```
docker build -t hotel:layers -t hotel:hotel-layers .
```

### 2. Uber-JAR (Legacy)
Классическая сборка «все-в-одном».
**Deprecated** (2026): Избыточный размер пересылаемых данных при минимальных изменениях в коде.
```
docker build -f Dockerfile_uber_jar -t hotel:hotel-uber .
```

### 3. Axiom (Alpaquita + Musl)
Оптимизированная слоеная сборка на базе Axiom образа. Разделение зависимостей и прикладного кода позволяет эффективно использовать Docker Cache, минимизируя трафик при CI/CD.
- **Base Image**: `bellsoft/liberica-runtime-alpine` (или Alpaquita).
- **Benefit**: Баланс между скоростью сборки и эффективностью кеширования.
```
docker build -f Dockerfile_axiom -t hotel:hotel-axiom .
```

### 4. Axiom (musl) Native Image Kit (NIK)
Компиляция в нативный бинарник с использованием **musl libc**.
- **Build Metrics**: ~13.3 мин (на i7-3720QM). Требует 8GB+ RAM для фазы анализа графа объектов.
- **Startup**: **1.566s** (Ready to serve).
- **Security**: Исполнение в среде **Alpaquita Cloud Native OS** от не-привилегированного пользователя (Non-root).
- **Verdict**: Идеально для масштабируемых микросервисов и Serverless.
```
docker build -f Dockerfile_axiom_native -t hotel:hotel-axiom-native .
docker build -f Dockerfile_axiom_native -t hotel:hotel-native .
```

### 5. Vanilla (debian:bookworm-slim) Native
Эталонный GraalVM на базе GLIBC (Debian Slim).
- **Build Metrics**: ~20.6 мин (на i7-3720QM) (в 1.8 раза медленнее Axiom). Требует 8GB+ RAM для фазы анализа графа объектов.
- **Startup**: **1,276s** (Агрессивная оптимизация ценой времени сборки).
- **Issues**: Нестабильность сетевых загрузок при сборке и увеличенный размер итогового образа из-за веса Debian-слоев
```
docker build -f Dockerfile_vanilla_native -t hotel:hotel-vanilla-native .
```

### 6. Vanilla (debian:bookworm-slim) Native with PGO
Future Work.
- **Status**: Исследование в процессе.
- **Target**: Внедрение **PGO (Profile Guided Optimization)** для достижения лимитов памяти в **<40** MB и сокращения времени отклика на 15-20%.
```
docker build -f Dockerfile_vanilla_native_pgo -t hotel:hotel-vanilla-native-pgo .
```

## Сводный результат
### Результаты всех сборок. Статистика по работе в Docker контейнерах.
Ниже приведён результат собранных образов, размеры.
- Размеры образов:

| REPOSITORY | TAG                      | IMAGE ID      | BUILDING TIME (s) | SIZE  |
|------------|--------------------------|---------------|-------------------|-------|
| hotel      | hotel-layers             | ae26d29918f9  | 7.3               | 460MB |
| hotel      | hotel-uber               | 4cabf402d48b  | 8.5               | 456MB |
| hotel      | hotel-axiom              | af088597f55a  | 12.8              | 362MB |
| hotel      | hotel-axiom-native       | bd2e31f8d511  | 800.1             | 317MB |
| hotel      | hotel-vanilla-native     | abd85388adfc  | 1237.8            | 443MB |
| hotel      | hotel-vanilla-native-pgo | -             | -                 | -     |

- Ниже представлен результат команды Docker stats (1-й запуск).

Внимание заслуживают показатели MEM USAGE и PIDS (количество системных потоков)
<p> 1-й запуск:

| CONTAINER ID | NAME                     | CPU % | MEM USAGE / LIMIT   | MEM % | NET I/O         | BLOCK I/O       | PIDS |
|--------------|--------------------------|-------|---------------------|-------|-----------------|-----------------|------|
| 68dad70a5e17 | hotel-layers             | 3.24% | 323.4MiB / 15.58GiB | 2.03% | 25.8kB / 26kB   | 8.19kB / 61.4kB | 55   |
| bd3f0ffd16ef | hotel-uber               | 2.94% | 350.6MiB / 15.58GiB | 2.20% | 24.2kB / 25.4kB | 0B / 49.2kB     | 53   |
| c209959f86a4 | hotel-axiom              | 0.29% | 399.4MiB / 15.58GiB | 2.50% | 24.1kB / 25.3kB | 0B / 49.2kB     | 55   |
| a1c22e9f5892 | hotel-axiom-native       | 0.05% | 49.08MiB / 15.58GiB | 0.31% | 22.7kB / 24.2kB | 0B / 0B         | 26   |
| 3e62c302c13d | hotel-vanilla-native     | 0.03% | 34.54MiB / 15.58GiB | 0.22% | 26.5kB / 25.7kB | 0B / 0B         | 25   |
| -            | hotel-vanilla-native-pgo | -     | -                   | -     | -               | -               | -    |

- Ниже представлен результат значений времени запуска и latency по запросу-ответу (1-й запуск).

| CONTAINER ID | NAME                     | Started(s) | process running (s) | Latency 1 request (ms) | Latency 2 request (ms) |
|--------------|--------------------------|------------|---------------------|------------------------|------------------------|
| 68dad70a5e17 | hotel-layers             | 15.524     | 16.461              | 1176                   | 404                    |
| bd3f0ffd16ef | hotel-uber               | 16.81      | 18.648              | 1019                   | 381                    |
| c209959f86a4 | hotel-axiom              | 13.8       | 14.763              | 1035                   | 403                    |
| a1c22e9f5892 | hotel-axiom-native       | 1.566      | 1.592               | 1255                   | 388                    |
| 3e62c302c13d | hotel-vanilla-native     | 1.357      | 1.377               | 912                    | 436                    |
| -            | hotel-vanilla-native-pgo | -          | -                   | -                      | -                      |

- Ниже представлен результат команды Docker stats (2-й запуск).

Внимание заслуживают показатели MEM USAGE и PIDS (количество системных потоков)
<p> 2-й запуск:

| CONTAINER ID | NAME                     | CPU % | MEM USAGE / LIMIT   | MEM % | NET I/O         | BLOCK I/O   | PIDS |
|--------------|--------------------------|-------|---------------------|-------|-----------------|-------------|------|
| 68dad70a5e17 | hotel-layers             | 0.39% | 327.9MiB / 15.58GiB | 2.05% | 36.4kB / 39.1kB | 0B / 32.8kB | 55   |
| bd3f0ffd16ef | hotel-uber               | 3.36% | 346.8MiB / 15.58GiB | 2.17% | 37.9kB / 39.5kB | 0B / 57.3kB | 53   |
| c209959f86a4 | hotel-axiom              | 0.28% | 395.1MiB / 15.58GiB | 2.48% | 37.9kB / 39.5kB | 0B / 49.2kB | 54   |
| a1c22e9f5892 | hotel-axiom-native       | 0.12% | 49.61MiB / 15.58GiB | 0.31% | 36.4kB / 38.6kB | 0B / 0B     | 26   |
| 3e62c302c13d | hotel-vanilla-native     | 2.80% | 36.06MiB / 15.58GiB | 0.23% | 36.4kB / 38.7kB | 0B / 0B     | 26   |
| -            | hotel-vanilla-native-pgo | -     | -                   | -     | -               | -           | -    |

- Ниже представлен результат значений времени запуска и latency по запросу-ответу (2-й запуск).

| CONTAINER ID | NAME                     | Started(s) | process running (s) | Latency 1 request (ms) | Latency 2 request (ms) |
|--------------|--------------------------|------------|---------------------|------------------------|------------------------|
| 68dad70a5e17 | hotel-layers             | 15.227     | 15.853              | 1121                   | 411                    |
| bd3f0ffd16ef | hotel-uber               | 16.628     | 17.996              | 1212                   | 453                    |
| c209959f86a4 | hotel-axiom              | 14.411     | 15.09               | 1141                   | 451                    |
| a1c22e9f5892 | hotel-axiom-native       | 1.458      | 1.472               | 879                    | 435                    |
| 3e62c302c13d | hotel-vanilla-native     | 1.276      | 1.286               | 864                    | 451                    |
| -            | hotel-vanilla-native-pgo | -          | -                   | -                      | -                      |


Необходимо отметить, что скрость запуска образов с JRE составила 13,5 - 18,5 секунд
Скорость запуска нативных образов - 1,2 - 1,5 секунд

### Итоги в виде таблицы
JRE (Axiom) vs JRE (Temurin) vs Axiom NIK (Pro) vs Vanilla Native
Конфигурация: Java 21, Spring Boot 3.5.x, Hibernate 6.6 (Bytecode Enhancement), Project Loom, Postgres 17, MongoDB 7, Kafka 6.
Железо: Intel i7-3720QM (4 Cores / 8 Threads), 32.0 GB RAM, Storage SSD, OS Win 10, Docker Desktop

| Метрика         | JRE (Axiom/Layers) | JRE (Temurin/Layers) | Axiom NIK Native     | Vanilla Native (Oracle) | Лучший результат        |
|-----------------|--------------------|----------------------|----------------------|-------------------------|-------------------------|
| Время сборки    | 12.8 сек           | 7.3 сек              | 13.3 мин             | 20.6 мин                | JRE (Instant)           |
| Размер образа   | 362 MiB            | 460 MiB              | **317 MiB**          | 443 MiB                 | Axiom NIK (-31%)        |
| Старт (Startup) | 16.03 сек          | 22.54 сек            | 1.458 сек            | **1.276 сек**           | Vanilla (Record)        |
| RAM (Idle)      | 427.1 MiB          | 386.0 MiB            | 49.6 MiB             | **38.1 MiB**            | Vanilla (-90%)          |
| Потоки (PIDS)   | 55                 | 55                   | **26**               | **26**                  | Native (в 2 раза легче) |
| 1-й Latency     | 1141 ms            | 1121 ms              | 879 ms               | **864 ms**              | Vanilla                 |
| ОС (Runtime)	   | BellSoft / Alpine  | Debian-based         | **Alpaquita (musl)** | Debian-slim             | Alpaquita (Ultra-light) |

### Краткие выводы
- **Экстремальная плотность (Density)**: Переход на Native Image позволил сократить потребление оперативной памяти с **386-427** МБ до **36-49 МБ**. Это обеспечивает **10-кратный выигрыш** в плотности размещения сервисов. На одном стандартном узле теперь можно запустить до 10 инстансов Hotel вместо одного.
- **Эффективность Project Loom**: Сочетание виртуальных потоков (Loom) и нативного рантайма сократило количество системных потоков (PIDS) более чем в **2 раза** (с 55 до 26). Это минимизирует накладные расходы ОС на переключение контекста и делает систему устойчивой к взрывным нагрузкам.
- **AOT-оптимизация Hibernate**: Использование Bytecode Enhancement в связке с GraalVM Metadata Repository позволило полностью сохранить функционал ленивой загрузки (Lazy Loading) в нативном бинарнике, устранив типичные для Native-режима ошибки инициализации прокси.
- **Инфраструктурный лидер**: Axiom NIK (Alpaquita/musl) подтвердил превосходство в компактности артефакта, сэкономив **126 МБ** дискового пространства по сравнению с Vanilla (Debian/glibc). При этом Axiom собирается на **35% быстрее** конкурента, что критично для скорости CI/CD пайплайнов.

### Экономические выводы
- **TCO (Total Cost of Ownership)**: Сокращение требований к RAM на 90% позволяет радикально снизить затраты на облачную инфраструктуру (AWS/Azure/Yandex Cloud).
- **Scale-to-Zero Ready**: Несмотря на тяжелый реляционный стек, время старта сокращено с 13 секунд до **~1 секунды**. Это делает возможным использование сервиса в Serverless-архитектурах.
- **Evolution Move**: В ходе R&D было принято архитектурное решение о выносе миграций (Liquibase) во внешние init-контейнеры. Это позволило добиться стабильности AOT-сборки и предсказуемого времени прогрева.