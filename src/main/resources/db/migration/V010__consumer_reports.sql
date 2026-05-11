-- =========================
-- Consumer report configuration
-- =========================
CREATE TABLE IF NOT EXISTS vrs.consumer_report (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fleet_id    BIGINT NOT NULL REFERENCES vrs.fleet(id),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Fields included in a consumer report (mapped from source field definitions)
CREATE TABLE IF NOT EXISTS vrs.consumer_report_field (
    id                       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    consumer_report_id       BIGINT NOT NULL REFERENCES vrs.consumer_report(id),
    field_definition_id      BIGINT REFERENCES vrs.report_field_definition(id),
    custom_field_id          BIGINT REFERENCES vrs.fleet_custom_field(id),
    display_name             VARCHAR(255),
    display_order            INTEGER NOT NULL DEFAULT 0,
    format_hint              VARCHAR(100),
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT consumer_field_source_check CHECK (
        (field_definition_id IS NOT NULL AND custom_field_id IS NULL) OR
        (field_definition_id IS NULL AND custom_field_id IS NOT NULL)
    )
);

-- =========================
-- Delivery subscriptions
-- =========================
CREATE TABLE IF NOT EXISTS vrs.consumer_subscription (
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    consumer_report_id BIGINT NOT NULL REFERENCES vrs.consumer_report(id),
    delivery_method    VARCHAR(20) NOT NULL,
    config             TEXT,
    active             BOOLEAN NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT consumer_subscription_method_check CHECK (
        delivery_method IN ('EMAIL', 'PDF', 'EXCEL', 'PULL_API', 'PUSH_API')
    )
);

-- =========================
-- Delivery audit log
-- =========================
CREATE TABLE IF NOT EXISTS vrs.delivery_audit_log (
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    consumer_report_id BIGINT NOT NULL REFERENCES vrs.consumer_report(id),
    vessel_id          BIGINT NOT NULL REFERENCES vrs.vessel(id),
    delivered_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delivery_method    VARCHAR(20) NOT NULL,
    status             VARCHAR(20) NOT NULL,
    error_message      TEXT,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
