-- =========================
-- Vessel name history
-- =========================
CREATE TABLE IF NOT EXISTS vrs.vessel_name_history (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vessel_id  BIGINT NOT NULL REFERENCES vrs.vessel(id),
    name       VARCHAR(255) NOT NULL,
    valid_from TIMESTAMP NOT NULL,
    valid_to   TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed name history from current vessel names
INSERT INTO vrs.vessel_name_history (vessel_id, name, valid_from)
SELECT id, name, created_at FROM vrs.vessel;

-- =========================
-- Vessel company management history
-- =========================
CREATE TABLE IF NOT EXISTS vrs.vessel_company_history (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vessel_id  BIGINT NOT NULL REFERENCES vrs.vessel(id),
    company_id BIGINT NOT NULL REFERENCES vrs.company(id),
    valid_from TIMESTAMP NOT NULL,
    valid_to   TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add current company to vessel table
ALTER TABLE vrs.vessel
    ADD COLUMN current_company_id BIGINT REFERENCES vrs.company(id);

-- Add master user to vessel table
ALTER TABLE vrs.vessel
    ADD COLUMN master_id BIGINT REFERENCES vrs.user_account(id);
