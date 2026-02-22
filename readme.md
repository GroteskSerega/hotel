# Hotel service: High-Performance AOT Reference
Микросервис бронирования отелей с возможностью управлять контентом через административную панель с ролевой моделью доступа (RBAC), оптимизированный для запуска в среде **Axiom NIK**.

## Core Features
- **RBAC Model**: Предустановленный ADMIN для инициализации системы, поддержка ролей ADMIN и USER.
- **Async Engine**: Стек на базе Spring MVC с поддержкой Project Loom.
- **Cloud Native**: Полная готовность к нативной компиляции и работе в Read-Only файловых системах.
- **AOT-friendly Architecture**: Использование MapStruct, Static Metamodel и отказ от динамических прокси.
- **Data Integrity**: Использование TransactionSynchronizationManager для гарантии доставки событий в Kafka только после коммита в Postgres.
- **High-Density**:  Оптимизация Hibernate-запросов через @EntityGraph и Slice для минимизации Heap-памяти.
- **High-Perf JPA**: UUID v7, Specification API, Slice и @EntityGraph.

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

| Переменная         | Описание                         | Дефолтное значение |
|--------------------|----------------------------------|--------------------|
| APP_SERVER_PORT    | Порт публикации сервиса          | 8080               |
| APP_ADMIN_USERNAME | Логин системного администратора  | admin              |          
| APP_ADMIN_PASSWORD | Пароль системного администратора | admin              |
| DB_HOST            | Host подключения к Postgres      | localhost          |
| DB_PORT            | Port подключения к Postgres      | 5432               |
| DB_NAME            | База данных в Postgres           | hotel_db           |
| DB_SCHEMA          | Схема в Postgres                 | hotel_schema       |
| DB_USER            | Логин в Postgres                 | postgres           |
| DB_PASSWORD        | Пароль в Postgres                | postgres           |
| MONGODB_HOST       | Host подключения к MongoDB       | localhost          |
| MONGODB_PORT       | Port подключения к MongoDb       | 27017              |
| MONGODB_DBNAME     | База данных в MongoDB            | statistics-db      |
| MONGODB_USER       | Логин в MongoDB                  | root               |
| MONGODB_PASSWORD   | Пароль в MongoDB                 | root               |
| KAFKA_HOST         | Host подключения к Kafka         | localhost          |
| KAFKA_PORT         | Port подключения к Kafka         | 9092               |


## Deployment Options

Приложение подготовлено к работе в различных окружениях: от локальной разработки до высокоплотных кластеров.

### 1. Bare Metal / Local JRE
Для запуска классического JAR-файла (используется Axiom JDK 21+):
```bash
java -jar hotel.jar --spring.config.import=optional:file:./application.yml
```

### 2. Docker (Standalone)
```bash
docker run --rm -p 8080:8080 \
  --name hotel \
  -e APP_SERVER_PORT=8080 \
  -e APP_ADMIN_USERNAME=admni \
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
  -e KAFKA_HOST=localhost \
  -e KAFKA_PORT=9092 \
  hotel
```

### 3. Docker Compose (Full Stack)
```bash
# Развертывание стека в фоновом режиме
docker-compose up -d

# Мониторинг логов
docker-compose logs -f app
```

### 4. Native Image Execution (Axiom NIK)
```bash
./hotel
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

