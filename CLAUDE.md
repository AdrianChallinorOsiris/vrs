# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build / Run / Test

- Run app: `./mvnw spring-boot:run` (port 9001, requires PostgreSQL on `bastet:5432` per `application.yaml`)
- Run all tests: `./mvnw test`
- Run single test class: `./mvnw test -Dtest=VesselServiceTest`
- Run single test method: `./mvnw test -Dtest=VesselServiceTest#methodName`
- Test profile uses `application-test.yaml` (database `vrstest`)
- Swagger UI: `/swagger-ui.html` when running

## Architecture

Spring Boot 4 / Java 25 / PostgreSQL / Flyway / Spring Security / JPA. Base package `uk.co.osiris.vrs`.

**Per-domain package layout** — every domain is self-contained in its own package with: `Entity`, `Dto` + `RequestDto`/`UpdateDto`, `Repository` (extends `JpaRepository`), `Mapper` (entity↔DTO), `Service`, `Controller(s)`. Domains: `company/`, `vessel/`, `users_roles/`. New domain code must follow this pattern (see `agents.md`).

**Dual controllers per domain for role separation**: e.g. `AdminCompanyController` (`/admin/v1/companies`, ROLE_ADMIN) vs `UserCompanyController` (`/user/v1/companies`, ROLE_USER). Vessel controller uses fine-grained role checks (CHARTERER/MANAGER/OWNER/TECHNICAL_MANAGER) on write endpoints.

**Security** (`security/SecurityConfig.java`): form-login, CSRF disabled, BCrypt. Public: `/swagger-ui/**`, `/v3/api-docs/**`, `/auth/**`. `/user/**` → ROLE_USER, `/admin/**` → ROLE_ADMIN. Roles stored in DB with `ROLE_` prefix. `SessionUserDetails` loads users and validates active flag.

**No abstract base entity.** Each entity owns its own `@Id` (IDENTITY), `createdAt`/`updatedAt` with `@PrePersist`/`@PreUpdate`, and `equals`/`hashCode` per the rules in `agents.md`. Composite-key entities (e.g. `UserRole`) use `@IdClass` and equality on the composite fields, not `id`.

**JPA scan path**: `@EnableJpaRepositories("uk.co.osiris")` — explicit, not default.

## Database

Flyway migrations in `src/main/resources/db/migration/` (V001 companies/roles/users seeded with Osiris admin, V002 vessels/fleets, V003 data elements, V004 compendium, V005 cross-references). Schema `vrs`. `ddl-auto: validate` — schema changes go via new Flyway migration, never entity-only.

## Conventions

- **Lombok**: `@Getter`/`@Setter` on entities/models, `@Slf4j` for loggers. See `agents.md`.
- **Entity equals/hashCode**: mandatory pattern in `agents.md` — id-based equality, `getClass().hashCode()`.
- **IMO numbers**: validated via `vessel/ImoNumberValidator` (7-digit checksum, static utility — reuse, don't reimplement).
- **Tests**: JUnit 5 + Mockito. Controllers tested with standalone MockMvc; `@WebMvcTest` slices use `SecurityTestConfig`.

## Known issues

- `SecurityConfigTest` fails: `@WebMvcTest` loads `DataWebAutoConfiguration` requiring `entityManagerFactory` not present in slice. Documented in `agents.md`. Ask user for failure log before investigating.

## Coding rules 

- Keep each method small. A method should do one job well, and not try to do multiple jobs. 
- Use packages for each functional area. For example, everything to do with a vessel whould be in the .vessel package. This will include DTO defintions, service, controller, data enties, repositories, etc. 
- Create tests for every core method
- Use databases wisely
  - Use the "vrs" database for productions 
  - Use the "vrs_dev" database for development 
  - Use the "vrs_test" database for testing. This database can be recreated
- Use Lombok where appropriate 
- Use DTOs for data sent to/from the rest endpoints, but do not use Spring Mapstruct. Write your own custom mapper classes and methods for readability and code maintenance. 

