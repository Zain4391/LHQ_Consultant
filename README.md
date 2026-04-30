# LHQ Consultant — Backend

Spring Boot REST API backend for the LHQ Consultant platform — a legal services application that connects clients with lawyers, manages bookings, tracks cases, and collects reviews.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Persistence | Spring Data JPA + PostgreSQL |
| Caching | Spring Data Redis |
| Messaging | Spring AMQP + RabbitMQ |
| Validation | Spring Validation |
| Utilities | Lombok, DataFaker |
| Build | Maven |

## Prerequisites

- JDK 25
- Maven (or use the included `mvnw` / `mvnw.cmd` wrapper)
- PostgreSQL database
- Redis
- RabbitMQ (or Docker for the last two)

## Quick Start

1. Clone the repository:

```bash
git clone https://github.com/Zain4391/LHQ_Consultant.git
cd LHQ_Consultant
```

2. Configure database and application settings in `src/main/resources/application.properties`.

3. (Optional) Start Redis and RabbitMQ via Docker Compose:

```bash
docker compose up -d
```

4. Run the application:

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
./mvnw spring-boot:run
```

## Build and Test

Run tests:

```bash
./mvnw test
```

Build executable jar:

```bash
./mvnw clean package
```

## Infrastructure Services (Docker Compose)

| Service | Default Port |
|---|---|
| Redis | 6379 |
| RabbitMQ | 5672 |
| RabbitMQ Management UI | 15672 |

## Domain Modules

| Module | Responsibility |
|---|---|
| `auth` | Registration, login, and JWT refresh token flow |
| `user` | User profile management |
| `lawyer` | Lawyer profile and specialties |
| `booking` | Availability templates, time slots, and bookings |
| `cases` | Client-lawyer relationships and legal case tracking |
| `review` | Booking reviews with sentiment tracking |

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
