-- =========================
-- Vessel access token for master authentication
-- =========================
ALTER TABLE vrs.vessel
    ADD COLUMN access_token VARCHAR(64) UNIQUE NOT NULL DEFAULT gen_random_uuid()::VARCHAR;

-- =========================
-- Fleet manager role
-- =========================
INSERT INTO vrs.role (name, description)
VALUES ('ROLE_FLEET_MANAGER', 'Fleet manager - controls a fleet for their company');
