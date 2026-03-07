-- V1__initial_schema.sql
-- Initial schema for VRS project

-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS vrs AUTHORIZATION adrian;

-- Company table
CREATE TABLE  IF NOT EXISTS vrs.company (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert initial company
INSERT INTO vrs.company (name, email)
VALUES
  ('Osiris', 'info@osiris.co.uk');

-- Role table
CREATE TABLE  IF NOT EXISTS vrs.role (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert initial roles
INSERT INTO vrs.role (name, description)
VALUES
  ('ROLE_ADMIN', 'System administrator with full access'),
  ('ROLE_CHARTERER', 'Chartering manager'),
  ('ROLE_OWNER', 'Owner of the vessel'),
  ('ROLE_VESSEL', 'The vessel itself'),
  ('ROLE_AGENT', 'Port agent'),
  ('ROLE_BUNKER_SUPPLIER', 'Bunker supplier'),
  ('ROLE_TECHNICAL_MANAGER', 'Technical manager')
;
-- todo: 
-- add created_at
-- updated_at
-- created_by
-- updated_by

-- User table
CREATE TABLE  IF NOT EXISTS vrs.user_account (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    company_id BIGINT NOT NULL REFERENCES vrs.company(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert initial user
INSERT INTO vrs.user_account (email, password_hash, company_id)
SELECT 'adrian.challinor@osiris.co.uk', crypt('Beds1tterImage$', gen_salt('bf')), id 
FROM vrs.company
WHERE name = 'Osiris';

-- Join table for user-role mapping
CREATE TABLE  IF NOT EXISTS vrs.user_role (
    user_id BIGINT NOT NULL REFERENCES vrs.user_account(id),
    role_id BIGINT NOT NULL REFERENCES vrs.role(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO vrs.user_role (user_id, role_id)
SELECT U.id, R.id
FROM vrs.user_account U, vrs.role R
WHERE U.email = 'adrian.challinor@osiris.co.uk' 
AND R.name = 'ROLE_ADMIN';