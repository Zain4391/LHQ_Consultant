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

| Service                | Default Port |
| ---------------------- | ------------ |
| Redis                  | 6379         |
| RabbitMQ               | 5672         |
| RabbitMQ Management UI | 15672        |

## Domain Modules

| Module    | Responsibility                                      |
| --------- | --------------------------------------------------- |
| `auth`    | Registration, login, and JWT refresh token flow     |
| `user`    | User profile management                             |
| `lawyer`  | Lawyer profile and specialties                      |
| `booking` | Availability templates, time slots, and bookings    |
| `cases`   | Client-lawyer relationships and legal case tracking |
| `review`  | Booking reviews with sentiment tracking             |

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
