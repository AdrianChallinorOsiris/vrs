# VRS — Full Implementation Plan

## Context

GOAL.md defines a vessel reporting system where ship masters submit daily reports that are distributed to consumers. Current state: basic company/user/vessel management with session-based form-login. No JWT, no fleet Java code (DB tables exist), no vessel name/company history, no report forms, no consumer reports.

This plan covers everything needed to reach the full vision, organised into phases. Phases are roughly sequential — auth is foundational — but Phases 3–5 can be parallelised once Phase 2 is done.

---

## CLAUDE.md Fix (do first, trivial)

- CLAUDE.md says test DB is `vrs_test`; actual config (`src/test/resources/application-test.yaml`) uses `vrstest`. Update CLAUDE.md line.

---

## Phase 1 — JWT Authentication

**Why:** GOAL.md requires JWT (≤24 h lifetime) for all users. Masters get a special URL with a unique vessel token → JWT. Current form-login session auth must be replaced. No JWT library is in pom.xml yet.

### 1.1 Add dependency
- Add `io.jsonwebtoken:jjwt-api`, `jjwt-impl`, `jjwt-jackson` to `pom.xml`.

### 1.2 Vessel access token (migration V006)
- Add column `access_token VARCHAR(64) UNIQUE NOT NULL DEFAULT gen_random_uuid()` to `vessel` table.
- Update `Vessel` entity, `VesselDto`, `VesselMapper`.
- Admin/manager endpoint to regenerate a vessel's access token.

### 1.3 JWT infrastructure — new package `security/jwt/`
- `JwtTokenProvider` — generates and validates JWTs; configurable secret + 24 h expiry.
- `JwtAuthFilter` — `OncePerRequestFilter`; extracts `Authorization: Bearer <token>` header, validates, sets `SecurityContext`.

### 1.4 Update `SecurityConfig`
- Switch to stateless session (`SessionCreationPolicy.STATELESS`).
- Register `JwtAuthFilter` before `UsernamePasswordAuthenticationFilter`.
- Keep existing path rules (`/user/**`, `/admin/**`, etc.).

### 1.5 Auth endpoints — new package `auth/`
- `POST /auth/login` — `{email, password}` → JWT. Standard users only.
- `GET /auth/vessel/{token}` — vessel access token → JWT with `ROLE_VESSEL` + `vesselId` claim. This is the "special URL" the master uses.
- Classes: `AuthController`, `AuthService`, `LoginRequestDto`, `TokenResponseDto`.

### 1.6 Roles
- Add `ROLE_FLEET_MANAGER` to the `role` table (migration V006). GOAL.md distinguishes ship manager (fleet + vessel admin) from fleet manager (controls one fleet).
- Existing roles map: ROLE_ADMIN=admin, ROLE_MANAGER=ship manager, ROLE_FLEET_MANAGER=fleet manager, ROLE_USER=user, ROLE_VESSEL=master.

### Verification
- `POST /auth/login` with seeded admin creds → 200 + token.
- `GET /auth/vessel/{token}` with valid vessel token → 200 + token with vesselId.
- Protected endpoint without token → 401.
- Expired token → 401.

---

## Phase 2 — Vessel Data Model

**Why:** GOAL.md requires vessel name history (names change over time), vessel-company management history (company changes over time), correct vessel types, and exactly one master per vessel.

### 2.1 Vessel types (migration V007)
- Replace current types (Tanker, Container Ship, etc.) with GOAL.md list:
  - Crude Oil Tanker, Product Tanker, Chemical Tanker, Dry Cargo, Other.
- Any existing vessels referencing old types → map to `Other` (or log warning).

### 2.2 Vessel name history (migration V008)
- New table `vessel_name_history`:
  ```
  id           BIGINT PK IDENTITY
  vessel_id    BIGINT NOT NULL FK → vessel.id
  name         VARCHAR(255) NOT NULL
  valid_from   TIMESTAMP NOT NULL
  valid_to     TIMESTAMP (nullable — null = current)
  created_at / updated_at
  ```
- Migrate existing `vessel.name` rows into `vessel_name_history` with `valid_from = vessel.created_at`, `valid_to = NULL`.
- Keep `vessel.name` as a denormalised "current name" for performance (updated on each rename).
- New `vessel/VesselNameHistory` entity, `VesselNameHistoryRepository`.
- `VesselService.rename(id, newName)` — closes current history row, opens new one, updates `vessel.name`.
- Include name history list in `VesselDto`.

### 2.3 Vessel-company management history (migration V008)
- New table `vessel_company_history`:
  ```
  id           BIGINT PK IDENTITY
  vessel_id    BIGINT NOT NULL FK → vessel.id
  company_id   BIGINT NOT NULL FK → company.id
  valid_from   TIMESTAMP NOT NULL
  valid_to     TIMESTAMP (nullable)
  created_at / updated_at
  ```
- Add `current_company_id BIGINT FK → company.id` to `vessel` table (denormalised current value).
- New entity `vessel/VesselCompanyHistory`, repo, service methods.
- `VesselService.assignToCompany(vesselId, companyId)` — closes old history row, opens new one.

### 2.4 Master assignment (migration V008)
- Add `master_id BIGINT FK → user_account.id` (nullable) to `vessel` table. One master per vessel.
- `VesselService.assignMaster(vesselId, userId)` — validates user has `ROLE_VESSEL`.
- On assign, system can generate/send the unique vessel access token URL.

### 2.5 Controller / DTO updates
- New endpoints on `VesselController`:
  - `POST /vessels/v1/{id}/rename` — MANAGER role.
  - `POST /vessels/v1/{id}/company` — ADMIN role (company management).
  - `POST /vessels/v1/{id}/master` — MANAGER role.
  - `POST /vessels/v1/{id}/token/regenerate` — MANAGER role.
- `VesselDto` extended: name history, current company, master user.

### Verification
- Rename vessel → history row created, `vessel.name` updated.
- Assign to new company → old history row closed, new opened.
- Assign master → `vessel.master_id` set, ROLE_VESSEL validated.
- Fetch vessel → DTO includes history.

---

## Phase 3 — Fleet Management (Java code)

**Why:** V002 migration already created `fleet` and `vessel_fleet_assignment` tables. No Java code exists.

### 3.1 Fleet domain — new package `fleet/`
- `Fleet` entity (id, company_id FK, name, description, timestamps).
- `FleetRepository extends JpaRepository`.
- `FleetDto`, `FleetRequestDto`.
- `FleetMapper`.
- `FleetService` — CRUD + list vessels in fleet.
- `AdminFleetController` (`/admin/v1/fleets`) — ROLE_MANAGER: create, update, delete fleet.
- `UserFleetController` (`/user/v1/fleets`) — ROLE_USER: list, get fleet.

### 3.2 Vessel-fleet assignment
- `VesselFleetAssignment` entity (id, vessel_id FK, fleet_id FK, valid_from, valid_to, timestamps).
- `VesselFleetAssignmentRepository`.
- `VesselFleetAssignmentDto`.
- Endpoints:
  - `POST /admin/v1/fleets/{fleetId}/vessels` — assign vessel (ROLE_MANAGER/FLEET_MANAGER).
  - `DELETE /admin/v1/fleets/{fleetId}/vessels/{vesselId}` — remove vessel (close valid_to).
  - `GET /user/v1/fleets/{fleetId}/vessels` — list current fleet vessels (ROLE_USER).

### Verification
- Create fleet → persisted, returned in list.
- Assign vessel → assignment row created.
- Remove vessel → `valid_to` set, vessel no longer in current fleet.
- `./mvnw test` passes.

---

## Phase 4 — Report Forms Data Model

**Why:** Core purpose of VRS — masters submit NoonDay, Bunkers, OffHire, Arrival, InPort, Departure reports. Fields are typed. Standard fields from IMO Compendium are mandatory; fleets can add custom fields.

### 4.1 Report types (migration V009)
- New table `report_type` (id, name, description, active).
- Seed: NoonDay, Bunkers, OffHire, Arrival, InPort, Departure.
- New `report/ReportType` entity, repo.

### 4.2 Field type catalogue (migration V009)
- New table `field_type` (id, name) — seed: STRING, TEXT, INTEGER, FLOAT, RADIO, CHECKBOX, SELECT, DATETIME, DURATION, LOCATION, TIMEZONE.
- New table `report_field_definition` (id, report_type_id FK, meta_data_element_id FK nullable, name, label, field_type_id FK, mandatory, display_order).
  - IMO Compendium fields reference `meta_data_element`; custom fields have null `meta_data_element_id`.

### 4.3 Fleet-specific custom fields (migration V009)
- New table `fleet_custom_field` (id, fleet_id FK, report_type_id FK, name, label, field_type_id FK, display_order).
- Ship manager/fleet manager can add via API.

### 4.4 Report submission (migration V009)
- New table `report` (id, vessel_id FK, report_type_id FK, submitted_at, submitted_by FK → user_account.id, status VARCHAR — DRAFT/SUBMITTED).
- New table `report_value` (id, report_id FK, field_definition_id FK, value TEXT).
  - For complex types (LOCATION, code lists) value is JSON string.
- New package `report/`: `Report`, `ReportValue`, `ReportType`, repos, `ReportService`, `ReportController`.
- Endpoints:
  - `POST /vessel/v1/reports` — ROLE_VESSEL — submit report for the vessel identified by JWT claim.
  - `GET /user/v1/reports?vesselId=&reportType=&from=&to=` — ROLE_USER — query submitted reports.

### Verification
- Master JWT → submit NoonDay report → `report` + `report_value` rows created.
- Missing mandatory field → 400 validation error.
- Fleet custom field appears in form definition for that fleet's vessels.
- `./mvnw test` passes.

---

## Phase 5 — Consumer Reports & Delivery

**Why:** Companies consume vessel data. Fleet managers configure which fields/reports they want. Delivery via email, PDF, Excel, or API. Audit log required.

### 5.1 Consumer report configuration (migration V010)
- New table `consumer_report` (id, fleet_id FK, name, description).
- New table `consumer_report_field` (id, consumer_report_id FK, source_field_definition_id FK, display_name, display_order, format_hint).
- Fleet manager configures via API.

### 5.2 Delivery subscriptions (migration V010)
- New table `consumer_subscription` (id, consumer_report_id FK, delivery_method ENUM(EMAIL, PDF, EXCEL, PULL_API, PUSH_API), config JSONB, active).
- `config` examples:
  - EMAIL: `{"to": "ops@acme.com"}`
  - PULL_API: `{"webhook_key": "<secret>"}`
  - PUSH_API: `{"endpoint": "https://acme.com/vrs", "headers": {...}}`

### 5.3 Delivery infrastructure — package `consumer/`
- `ConsumerReportService` — assembles report data from `report_value` rows per consumer config.
- Delivery handlers (one per method):
  - `EmailDeliveryHandler` — Spring Mail, plain text + HTML.
  - `PdfDeliveryHandler` — Apache PDFBox.
  - `ExcelDeliveryHandler` — Apache POI.
  - `PullApiController` — `GET /api/v1/reports/{consumerReportId}?vesselId=&from=&to=`, key validated from `Authorization: ApiKey <key>` header.
  - `PushApiService` — triggered on report submission; bespoke per client.

### 5.4 Audit log (migration V010)
- New table `delivery_audit_log` (id, consumer_report_id FK, vessel_id FK, delivered_at, delivery_method, status, error_message).
- Written on every delivery attempt (success or failure).

### Verification
- Configure consumer report for fleet → delivery fires on next report submission.
- Pull API with valid key → 200 + JSON. Invalid key → 401.
- Audit log entry created for each delivery.
- `./mvnw test` passes.

---

## Critical Files

| File | Relevance |
|------|-----------|
| `src/main/resources/db/migration/V002__vessels.sql` | Fleet schema — reference when writing Fleet entity |
| `src/main/java/uk/co/osiris/vrs/security/SecurityConfig.java` | Replace form-login with JWT filter |
| `src/main/java/uk/co/osiris/vrs/vessel/Vessel.java` | Add access_token, master_id, current_company_id |
| `src/main/java/uk/co/osiris/vrs/vessel/VesselService.java` | Extend with rename, company assign, master assign |
| `src/test/resources/application-test.yaml` | Test DB config (vrstest) |
| `CLAUDE.md` | Fix `vrs_test` → `vrstest` |

## Migration sequence
```
V006 — JWT vessel access token + ROLE_FLEET_MANAGER
V007 — Replace vessel types
V008 — Vessel name history, vessel-company history, master_id on vessel
V009 — Report types, field types, report submission tables
V010 — Consumer reports, delivery subscriptions, audit log
```
