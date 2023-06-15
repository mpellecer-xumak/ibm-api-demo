ALTER TABLE ${schemaName}.client_append
    ADD COLUMN check_do_not_call BOOLEAN DEFAULT TRUE;
ALTER TABLE ${schemaName}.client_append
    ADD COLUMN return_do_not_call_phone BOOLEAN DEFAULT FALSE;

ALTER TABLE ${schemaName}.append
    ADD COLUMN check_do_not_call BOOLEAN DEFAULT TRUE;
ALTER TABLE ${schemaName}.append
    ADD COLUMN return_do_not_call_phone BOOLEAN DEFAULT FALSE;

DROP VIEW IF EXISTS ${schemaName}.client_append_view;
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
    COALESCE(client_append.enabled, append.enabled) enabled,
    COALESCE(client_append.check_do_not_call, append.check_do_not_call) check_do_not_call,
    COALESCE(client_append.return_do_not_call_phone, append.return_do_not_call_phone) return_do_not_call_phone
FROM ${schemaName}.append
         LEFT JOIN ${schemaName}.client_append
                   ON client_append.append_id = append.id
         LEFT JOIN ${schemaName}.client
                   ON client.id = client_append.client_id OR append.enabled
ORDER BY client.client_id;