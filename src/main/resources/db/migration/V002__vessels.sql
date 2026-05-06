
-- =========================
-- Vessel Type
-- =========================
CREATE TABLE IF NOT EXISTS vrs.vessel_type (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO vrs.vessel_type (name) VALUES ('Tanker'), ('Container Ship'), ('General Cargo Ship'), ('Passenger Ship'), ('Ro-Ro Ship'), ('Other');

-- =========================
-- Vessel 
-- =========================
CREATE TABLE IF NOT EXISTS vrs.vessel(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    imo_number VARCHAR(7) NOT NULL UNIQUE CHECK (imo_number ~ '^[0-9]{7}$'),
    vessel_type_id BIGINT NOT NULL REFERENCES vrs.vessel_type(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Fleet
-- =========================
CREATE TABLE IF NOT EXISTS vrs.fleet (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES vrs.company(id), 
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Fleet Vessel (Fleet consists of vessels)
-- =========================
CREATE TABLE IF NOT EXISTS vrs.vessel_fleet_assignment (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    vessel_id BIGINT NOT NULL
        REFERENCES vrs.vessel(id),

    fleet_id BIGINT NOT NULL
        REFERENCES vrs.fleet(id),

    valid_from TIMESTAMP NOT NULL,
    valid_to   TIMESTAMP
);
