# VRS — Vessel Reporting System

A Spring Boot backend for vessel reporting, fleet management, and maritime operations.

## Tech Stack

- **Java 25** with **Spring Boot 4**
- **PostgreSQL** with **Flyway** migrations
- **Spring Data JPA** / Hibernate
- **Spring Security**
- **SpringDoc OpenAPI** (Swagger UI)

## Prerequisites

- Java 25
- Maven 3.9+
- PostgreSQL

## Getting Started

1. **Create the database** and ensure PostgreSQL is running.

2. **Configure the datasource** in `src/main/resources/application.yaml`:

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/your_database
       username: your_username
       password: your_password
   ```

3. **Run the application**:

   ```bash
   ./mvnw spring-boot:run
   ```

4. **API documentation** is available at `/swagger-ui.html` when the app is running.

## Project Structure

```
src/main/java/uk/co/osiris/vrs/
├── VrsApplication.java
├── company/           # Company entities and repositories
├── users_roles/       # User accounts, roles, and permissions
├── security/          # Security configuration
└── ...
```

## Database

Schema and migrations live in `src/main/resources/db/migration/`:

- **V001** — Companies, roles, users, user-role mapping
- **V002** — Vessel types, vessels, fleets, fleet-vessel assignments
- **V003** — Data elements
- **V004** — Compendium
- **V005** — Cross references

Flyway uses the `vrs` schema by default.

## Running Tests

```bash
./mvnw test
```

## License

Proprietary — Osiris
