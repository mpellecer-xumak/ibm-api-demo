CREATE TABLE ${schemaName}.client
(
    id            SERIAL PRIMARY KEY,
    client_id     VARCHAR(256) NOT NULL,
    sub_client_id VARCHAR(256),
    "key"           VARCHAR(32)  NOT NULL UNIQUE,
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    UNIQUE (client_id, sub_client_id)
);

CREATE TABLE ${schemaName}.identity_resolution_client_config
(
    id                         SERIAL PRIMARY KEY,
    client_id                  INT REFERENCES ${schemaName}.client (id),
    match_limit                INT,
    last_name_threshold        INT,
    resident_threshold         INT,
    individual_threshold       INT,
    household_threshold        INT,
    address_threshold          INT,
    date_of_birth_range_years  INT,
    insight_batch_limit        INT,
    std_address_enabled        BOOLEAN,
    use_graph_id               BOOLEAN,
    enable_keyring             BOOLEAN,
    enable_fuzzy_gender_filter BOOLEAN,
    dpv_status_enabled         BOOLEAN
);

CREATE TABLE ${schemaName}.identity_resolution
(
    id                         SERIAL PRIMARY KEY,
    match_limit                INT     NOT NULL DEFAULT 1,
    last_name_threshold        INT     NOT NULL DEFAULT 95,
    resident_threshold         INT     NOT NULL DEFAULT 92,
    individual_threshold       INT     NOT NULL DEFAULT 88,
    household_threshold        INT     NOT NULL DEFAULT 85,
    address_threshold          INT     NOT NULL DEFAULT 99,
    date_of_birth_range_years  INT     NOT NULL DEFAULT 2,
    insight_batch_limit        INT     NOT NULL DEFAULT 1000,
    std_address_enabled        BOOLEAN NOT NULL DEFAULT TRUE,
    use_graph_id               BOOLEAN NOT NULL DEFAULT TRUE,
    enable_keyring             BOOLEAN NOT NULL DEFAULT FALSE,
    enable_fuzzy_gender_filter BOOLEAN NOT NULL DEFAULT TRUE,
    dpv_status_enabled         BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE ${schemaName}.bundle
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE ${schemaName}.attribute
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE ${schemaName}.bundle_attribute
(
    id           SERIAL PRIMARY KEY,
    bundle_id    INT REFERENCES ${schemaName}.bundle (id),
    attribute_id INT REFERENCES ${schemaName}.attribute (id)
);

CREATE TABLE ${schemaName}.human_insights_bundle
(
    id        SERIAL PRIMARY KEY,
    client_id INT REFERENCES ${schemaName}.client (id),
    bundle_id INT REFERENCES ${schemaName}.bundle (id)
);

CREATE TABLE ${schemaName}.aggregate
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(32) NOT NULL UNIQUE,
    enabled BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE ${schemaName}.aggregate_bundle
(
    id           SERIAL PRIMARY KEY,
    aggregate_id INT REFERENCES ${schemaName}.aggregate (id),
    bundle_id    INT REFERENCES ${schemaName}.bundle (id)
);

CREATE TABLE ${schemaName}.client_aggregate
(
    id           SERIAL PRIMARY KEY,
    client_id    INT REFERENCES ${schemaName}.client (id),
    aggregate_id INT REFERENCES ${schemaName}.aggregate (id),
    enabled      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE ${schemaName}.client_aggregate_bundle
(
    id                  SERIAL PRIMARY KEY,
    client_aggregate_id INT REFERENCES ${schemaName}.client_aggregate (id),
    bundle_id           INT REFERENCES ${schemaName}.bundle (id)
);

CREATE TABLE ${schemaName}.matcher
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE ${schemaName}.hierarchy
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE ${schemaName}.client_matcher
(
    id           SERIAL PRIMARY KEY,
    client_id    INT REFERENCES ${schemaName}.client (id),
    matcher_id   INT REFERENCES ${schemaName}.matcher (id),
    hierarchy_id INT REFERENCES ${schemaName}.hierarchy (id),
    match_level  INT     NOT NULL,
    index        INT     NOT NULL,
    enabled      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE ${schemaName}.platform_matcher
(
    id           SERIAL PRIMARY KEY,
    matcher_id   INT REFERENCES ${schemaName}.matcher (id),
    hierarchy_id INT REFERENCES ${schemaName}.hierarchy (id),
    match_level  INT     NOT NULL,
    index        INT     NOT NULL,
    enabled      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE ${schemaName}.suppression
(
    id                        SERIAL PRIMARY KEY,
    name                      VARCHAR(32) NOT NULL UNIQUE,
    resident_threshold        INT,
    household_threshold       INT,
    address_threshold         INT,
    date_of_birth_range_years INT,
    enabled                   BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE ${schemaName}.client_suppression
(
    id                        SERIAL PRIMARY KEY,
    client_id                 INT REFERENCES ${schemaName}.client (id),
    suppression_id            INT REFERENCES ${schemaName}.suppression (id),
    resident_threshold        INT,
    household_threshold       INT,
    address_threshold         INT,
    date_of_birth_range_years INT,
    enabled                   BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE ${schemaName}.deployment_version
(
    name          VARCHAR(32) PRIMARY KEY,
    color         VARCHAR(10) NOT NULL,
    date_modified TIMESTAMP
);
