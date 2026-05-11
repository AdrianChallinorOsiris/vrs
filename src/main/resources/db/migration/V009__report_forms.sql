-- =========================
-- Report types
-- =========================
CREATE TABLE IF NOT EXISTS vrs.report_type (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO vrs.report_type (name, description) VALUES
    ('NoonDay',   'Daily report sent at local noon'),
    ('Bunkers',   'Bunker fuel report (ROB and usage per grade)'),
    ('OffHire',   'Vessel off-hire / problem report'),
    ('Arrival',   'Report on arrival at port'),
    ('InPort',    'Abbreviated NoonDay report while in port'),
    ('Departure', 'Report on departure from port');

-- =========================
-- Field types
-- =========================
CREATE TABLE IF NOT EXISTS vrs.field_type (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO vrs.field_type (name) VALUES
    ('STRING'),
    ('TEXT'),
    ('INTEGER'),
    ('FLOAT'),
    ('RADIO'),
    ('CHECKBOX'),
    ('SELECT'),
    ('DATETIME'),
    ('DURATION'),
    ('LOCATION'),
    ('TIMEZONE');

-- =========================
-- Report field definitions
-- Standard fields (from IMO Compendium) or custom (no meta_data_element)
-- =========================
CREATE TABLE IF NOT EXISTS vrs.report_field_definition (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    report_type_id        BIGINT NOT NULL REFERENCES vrs.report_type(id),
    meta_data_element_id  BIGINT REFERENCES vrs.meta_data_element(id),
    name                  VARCHAR(100) NOT NULL,
    label                 VARCHAR(255) NOT NULL,
    field_type_id         BIGINT NOT NULL REFERENCES vrs.field_type(id),
    mandatory             BOOLEAN NOT NULL DEFAULT FALSE,
    display_order         INTEGER NOT NULL DEFAULT 0,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Fleet-specific custom fields
-- =========================
CREATE TABLE IF NOT EXISTS vrs.fleet_custom_field (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fleet_id       BIGINT NOT NULL REFERENCES vrs.fleet(id),
    report_type_id BIGINT NOT NULL REFERENCES vrs.report_type(id),
    name           VARCHAR(100) NOT NULL,
    label          VARCHAR(255) NOT NULL,
    field_type_id  BIGINT NOT NULL REFERENCES vrs.field_type(id),
    display_order  INTEGER NOT NULL DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Report instances (submitted by vessel masters)
-- =========================
CREATE TABLE IF NOT EXISTS vrs.report (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vessel_id       BIGINT NOT NULL REFERENCES vrs.vessel(id),
    report_type_id  BIGINT NOT NULL REFERENCES vrs.report_type(id),
    submitted_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_by    BIGINT REFERENCES vrs.user_account(id),
    status          VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Report field values
-- =========================
CREATE TABLE IF NOT EXISTS vrs.report_value (
    id                       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    report_id                BIGINT NOT NULL REFERENCES vrs.report(id),
    field_definition_id      BIGINT REFERENCES vrs.report_field_definition(id),
    custom_field_id          BIGINT REFERENCES vrs.fleet_custom_field(id),
    value                    TEXT,
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT report_value_field_check CHECK (
        (field_definition_id IS NOT NULL AND custom_field_id IS NULL) OR
        (field_definition_id IS NULL AND custom_field_id IS NOT NULL)
    )
);
