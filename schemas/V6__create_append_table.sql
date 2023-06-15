CREATE TABLE ${schemaName}.append
(
    id                              SERIAL PRIMARY KEY,
    name                            VARCHAR(32) NOT NULL UNIQUE,
    individual_threshold            INT,
    resident_threshold              INT,
    household_threshold             INT,
    address_threshold               INT,
    last_name_threshold             INT,
    date_of_birth_range_years       INT,
    enable_fuzzy_gender_filter      BOOLEAN NOT NULL DEFAULT FALSE,
    suppress_state_do_not_call      BOOLEAN NOT NULL DEFAULT FALSE,
    suppress_federal_do_not_call    BOOLEAN NOT NULL DEFAULT FALSE,
    enabled                         BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE ${schemaName}.client_append
(
    id                              SERIAL PRIMARY KEY,
    client_id                       INT REFERENCES ${schemaName}.client (id),
    append_id                       INT REFERENCES ${schemaName}.append (id),
    individual_threshold            INT,
    resident_threshold              INT,
    household_threshold             INT,
    address_threshold               INT,
    last_name_threshold             INT,
    date_of_birth_range_years       INT,
    enable_fuzzy_gender_filter      BOOLEAN NOT NULL DEFAULT FALSE,
    suppress_state_do_not_call      BOOLEAN NOT NULL DEFAULT FALSE,
    suppress_federal_do_not_call    BOOLEAN NOT NULL DEFAULT FALSE,
    enabled                         BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE OR REPLACE VIEW ${schemaName}.client_append_view
AS
SELECT
    client.client_id,
    client.sub_client_id,
    append.name,
    COALESCE(client_append.resident_threshold, append.resident_threshold) resident_threshold,
    COALESCE(client_append.household_threshold, append.household_threshold) household_threshold,
    COALESCE(client_append.address_threshold, append.address_threshold) address_threshold,
    COALESCE(client_append.date_of_birth_range_years, append.date_of_birth_range_years) date_of_birth_range_years,
    COALESCE(client_append.enabled, append.enabled) enabled
FROM ${schemaName}.append
         LEFT JOIN ${schemaName}.client_append
                   ON client_append.append_id = append.id
         LEFT JOIN ${schemaName}.client
                   ON client.id = client_append.client_id OR append.enabled
ORDER BY client.client_id;

-- Appends
INSERT INTO ${schemaName}.append(name) VALUES ('nameAddressToPhone');
INSERT INTO ${schemaName}.append(name, individual_threshold, last_name_threshold, date_of_birth_range_years) VALUES ('namePhoneToAddress', 88, 85, 999);
INSERT INTO ${schemaName}.append(name, individual_threshold, last_name_threshold, date_of_birth_range_years) VALUES ('nameEmailToAddress', 88, 85, 999);
INSERT INTO ${schemaName}.append(name, resident_threshold, household_threshold, date_of_birth_range_years) VALUES ('nameAddressToEmail', 92, 85, 999);
INSERT INTO ${schemaName}.append(name, resident_threshold, household_threshold, date_of_birth_range_years) VALUES ('householdMembers', 88, 85, 999);
INSERT INTO ${schemaName}.append(name, resident_threshold, household_threshold, date_of_birth_range_years, suppress_state_do_not_call, suppress_federal_do_not_call) VALUES ('nameEmailToPhone', 92, 85, 999, true, true);

-- WTD client appends
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (1, 1, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (1, 2, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (1, 3, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (1, 4, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (1, 5, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (1, 6, TRUE);

-- WTCE client appends
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (2, 1, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (2, 2, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (2, 3, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (2, 4, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (2, 5, TRUE);
INSERT INTO ${schemaName}.client_append(client_id, append_id, enabled) VALUES (2, 6, TRUE);
