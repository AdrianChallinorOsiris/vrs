-- =========================
-- Meta Data Source
-- =========================
CREATE TABLE IF NOT EXISTS vrs.meta_data_source (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO vrs.meta_data_source (name) VALUES ('IMO'), ('VRS'), ('Other');


-- =========================
-- Meta Data classes
-- =========================
CREATE TABLE IF NOT EXISTS vrs.meta_data_class (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    meta_data_source_id BIGINT NOT NULL REFERENCES vrs.meta_data_source(id),  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Meta Data Elements
-- =========================

CREATE TABLE IF NOT EXISTS vrs.meta_data_element (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    data_number VARCHAR(32) NOT NULL,
    name VARCHAR(255) NOT NULL,
    definition TEXT NOT NULL,
    code_list TEXT, 
    data_format varchar(128), 
    business_rule TEXT,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =========================
-- Meta Data Cross References
-- =========================
CREATE TABLE IF NOT EXISTS vrs.meta_data_cross_reference (
    meta_data_class_id BIGINT NOT NULL REFERENCES vrs.meta_data_class(id),
    meta_data_element_id BIGINT NOT NULL REFERENCES vrs.meta_data_element(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (meta_data_class_id, meta_data_element_id)
);
