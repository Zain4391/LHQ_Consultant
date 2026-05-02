# LHQ Consultant — Backend

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

Spring Boot REST API backend for the LHQ Consultant platform — a legal services application that connects clients with lawyers, manages bookings, tracks cases, and collects reviews.

## Tech Stack

<p align="left">
  <img src="https://skillicons.dev/icons?i=java" alt="Java" title="Java 25" />
  <img src="https://skillicons.dev/icons?i=spring" alt="Spring Boot" title="Spring Boot 4.0.6" />
  <img src="https://skillicons.dev/icons?i=postgres" alt="PostgreSQL" title="PostgreSQL" />
  <img src="https://skillicons.dev/icons?i=redis" alt="Redis" title="Redis" />
  <img src="https://skillicons.dev/icons?i=rabbitmq" alt="RabbitMQ" title="RabbitMQ" />
  <img src="https://skillicons.dev/icons?i=docker" alt="Docker" title="Docker" />
</p>

| Layer       | Technology                                                                                                                                                                                                                        |
| ----------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Language    | ![Java](https://img.shields.io/badge/Java-25-ED8B00?style=flat&logo=openjdk&logoColor=white)                                                                                                                                      |
| Framework   | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=flat&logo=spring-boot&logoColor=white)                                                                                                                 |
| Security    | ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=spring-security&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-jjwt_0.12.6-000000?style=flat&logo=jsonwebtokens&logoColor=white) |
| Persistence | ![JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat&logo=spring&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white)                       |
| Caching     | ![Redis](https://img.shields.io/badge/Redis-FF4438?style=flat&logo=redis&logoColor=white)                                                                                                                                         |
| Messaging   | ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=flat&logo=rabbitmq&logoColor=white)                                                                                                                                |
| Validation  | ![Spring Validation](https://img.shields.io/badge/Spring_Validation-6DB33F?style=flat&logo=spring&logoColor=white)                                                                                                                |
| Utilities   | ![Lombok](https://img.shields.io/badge/Lombok-pink?style=flat) ![DataFaker](https://img.shields.io/badge/DataFaker-2.3.1-blue?style=flat)                                                                                         |
| Build       | ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apache-maven&logoColor=white)                                                                                                                                  |

## Prerequisites

- **JDK 25** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven** (or use the included `mvnw` / `mvnw.cmd` wrapper)
- **PostgreSQL 12+** - [Download](https://www.postgresql.org/download/)
- **Docker & Docker Compose** (for Redis and RabbitMQ) - [Download](https://docs.docker.com/get-docker/)

## Setup Guide

### 1. Clone the Repository

```bash
git clone https://github.com/Zain4391/LHQ_Consultant.git
cd LHQ_Consultant/LHQ_Backend
```

### 2. Create PostgreSQL Database

Start PostgreSQL and create the database:

```bash
# Connect to PostgreSQL
psql -U postgres

# In PostgreSQL shell, create the database
CREATE DATABASE lhq_db;
\q
```

**Default credentials (for local development):**

- Host: `localhost`
- Port: `5432`
- Username: `postgres`
- Password: `Zain_2003`
- Database: `lhq_db`

> ⚠️ **Security Note:** Change credentials in production.

### 3. Start Infrastructure Services

Start Redis and RabbitMQ using Docker Compose:

```bash
docker compose up -d
```

Verify services are running:

```bash
docker ps
```

Expected output:
| Service | Port | Status |
|---------|------|--------|
| Redis | 6379 | Running |
| RabbitMQ | 5672 | Running |
| RabbitMQ UI | 15672 | Running |

**Access RabbitMQ Management UI:**

- URL: `http://localhost:15672`
- Username: `lhq_user`
- Password: `lhq_pass`

### 4. Build the Project

```bash
# Build the project and download dependencies
./mvnw clean install
```

Or on Windows:

```powershell
.\mvnw.cmd clean install
```

## Running Guide

### Development Environment

**Start the application:**

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
./mvnw spring-boot:run
```

The application will:

- Start on `http://localhost:8080`
- Connect to PostgreSQL at `localhost:5432/lhq_db`
- Use local profile (`application-local.properties`)
- Initialize database schema automatically (via Hibernate DDL auto)

**Check application health:**

```bash
curl http://localhost:8080/actuator/health
```

### Production Environment

Build production-ready JAR:

```bash
./mvnw clean package -DskipTests
```

Run with production profile:

```bash
java -jar target/LHQ_Backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

Ensure production database credentials are configured in `application-prod.properties`.

## Build and Test

### Run All Tests

```bash
./mvnw test
```

### Build Executable JAR

```bash
./mvnw clean package
```

Output: `target/LHQ_Backend-0.0.1-SNAPSHOT.jar`

### Run Tests with Coverage Report

```bash
./mvnw clean test jacoco:report
```

Report location: `target/site/jacoco/index.html`

## Infrastructure Services (Docker Compose)

The project uses Docker Compose to manage Redis and RabbitMQ services.

### Services Configuration

| Service                | Port  | Environment                     |
| ---------------------- | ----- | ------------------------------- |
| PostgreSQL             | 5432  | Local only (install separately) |
| Redis                  | 6379  | Container (Docker Compose)      |
| RabbitMQ AMQP          | 5672  | Container (Docker Compose)      |
| RabbitMQ Management UI | 15672 | Container (Docker Compose)      |

### Docker Compose Commands

```bash
# Start services in background
docker compose up -d

# View running services
docker compose ps

# View logs
docker compose logs -f

# Stop services
docker compose down

# Stop and remove volumes
docker compose down -v
```

### Verify Services

```bash
# Test Redis
redis-cli ping
# Expected: PONG

# Test RabbitMQ (port 5672)
curl localhost:5672
# Expected: Connection works

# Access RabbitMQ Management UI
# Browser: http://localhost:15672
# Credentials: lhq_user / lhq_pass
```

## Domain Modules

| Module    | Responsibility                                      |
| --------- | --------------------------------------------------- |
| `auth`    | Registration, login, and JWT refresh token flow     |
| `user`    | User profile management                             |
| `lawyer`  | Lawyer profile and specialties                      |
| `booking` | Availability templates, time slots, and bookings    |
| `cases`   | Client-lawyer relationships and legal case tracking |
| `review`  | Booking reviews with sentiment tracking             |

## Database Schema

![Database Schema](../diagrams/Schema.png)

## Troubleshooting

### Issue: "Connection refused" for PostgreSQL

**Solution:**

1. Verify PostgreSQL is installed and running:
   ```bash
   psql -U postgres -c "SELECT version();"
   ```
2. Check connection details in `application-local.properties`
3. Ensure database `lhq_db` exists

### Issue: "Unable to connect to Redis"

**Solution:**

```bash
# Check if Redis container is running
docker compose ps

# Restart Redis
docker compose restart redis

# Verify Redis is accessible
redis-cli ping
```

### Issue: "RabbitMQ connection failed"

**Solution:**

```bash
# Check if RabbitMQ container is running
docker compose ps

# View RabbitMQ logs
docker compose logs rabbitmq

# Restart RabbitMQ
docker compose restart rabbitmq
```

### Issue: "Port already in use"

**Solution:**

```bash
# Find process using port (example: 8080)
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Stop the process or use different port
java -jar target/LHQ_Backend-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Issue: Maven build fails

**Solution:**

```bash
# Clear Maven cache
./mvnw clean

# Update dependencies
./mvnw dependency:resolve

# Rebuild
./mvnw clean install
```

## API Documentation

Once the application is running, access API documentation:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`
- **Actuator:** `http://localhost:8080/actuator`

## Environment Variables

For production deployments, set these environment variables:

```bash
# Database
DB_URL=jdbc:postgresql://host:5432/lhq_db
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your_secret_key_min_32_chars
JWT_EXPIRATION=86400000

# Redis
REDIS_HOST=redis
REDIS_PORT=6379

# RabbitMQ
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=lhq_user
RABBITMQ_PASSWORD=lhq_pass
```

## Development Workflow

1. **Create a feature branch:**

   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make changes and test:**

   ```bash
   ./mvnw test
   ```

3. **Build locally:**

   ```bash
   ./mvnw clean package
   ```

4. **Run and verify:**

   ```bash
   ./mvnw spring-boot:run
   ```

5. **Commit and push:**
   ```bash
   git add .
   git commit -m "feat: description of changes"
   git push origin feature/your-feature-name
   ```

## Project Structure

```
LHQ_Backend/
├── src/
│   ├── main/
│   │   ├── java/com/LHQ_Backend/LHQ_Backend/
│   │   │   ├── auth/          # Authentication & JWT
│   │   │   ├── booking/       # Booking management
│   │   │   ├── cases/         # Case tracking
│   │   │   ├── config/        # Spring configuration
│   │   │   ├── lawyer/        # Lawyer profiles
│   │   │   ├── notification/  # Notifications
│   │   │   ├── review/        # Reviews & ratings
│   │   │   └── user/          # User management
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-local.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/              # Unit & integration tests
├── docs/
│   └── entity_documentation.md
├── pom.xml                    # Maven configuration
└── docker-compose.yml         # Redis & RabbitMQ services
```

## License

MIT License - See [LICENSE](LICENSE) file for details

See [`docs/entity_documentation.md`](docs/entity_documentation.md) for a full description of all JPA entities and their relationships.

## Project Structure

```text
LHQ_Consultant/
├── docs/                          # Entity and design documentation
├── src/
│   ├── main/
│   │   └── java/com/LHQ_Backend/LHQ_Backend/
│   │       ├── LhqBackendApplication.java
│   │       ├── auth/
│   │       ├── booking/
│   │       ├── cases/
│   │       ├── lawyer/
│   │       ├── review/
│   │       └── user/
│   └── test/
│       └── java/com/LHQ_Backend/LHQ_Backend/
├── pom.xml
├── mvnw
└── mvnw.cmd
```

Each domain module follows a consistent internal layout:

```text
<module>/
├── DTOs/
│   ├── Request/
│   └── Response/
├── entity/
├── enums/
├── repository/
└── service/
```

## Configuration Notes

- Never commit secrets (database credentials, JWT signing keys, etc.) to source control.
- Use environment variables or a local Spring profile (e.g. `application-local.properties`) to supply sensitive values.

## License

This project is licensed under the [MIT License](LICENSE).
