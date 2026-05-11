-- Replace vessel types with GOAL.md list
-- Vessels referencing old types are remapped to 'Other'

-- Temporarily add the new types
INSERT INTO vrs.vessel_type (name) VALUES
    ('Crude Oil Tanker'),
    ('Product Tanker'),
    ('Chemical Tanker'),
    ('Dry Cargo'),
    ('Other - General');

-- Remap any existing vessels to the 'Other - General' placeholder before deleting old types
UPDATE vrs.vessel
SET vessel_type_id = (SELECT id FROM vrs.vessel_type WHERE name = 'Other - General')
WHERE vessel_type_id IN (
    SELECT id FROM vrs.vessel_type WHERE name IN ('Tanker', 'Container Ship', 'General Cargo Ship', 'Passenger Ship', 'Ro-Ro Ship', 'Other')
);

-- Remove old types
DELETE FROM vrs.vessel_type WHERE name IN ('Tanker', 'Container Ship', 'General Cargo Ship', 'Passenger Ship', 'Ro-Ro Ship', 'Other');

-- Rename placeholder to 'Other'
UPDATE vrs.vessel_type SET name = 'Other' WHERE name = 'Other - General';
