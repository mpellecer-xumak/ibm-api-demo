DROP VIEW IF EXISTS ${schemaName}.client_append_view;
CREATE OR REPLACE VIEW ${schemaName}.client_append_view
AS
SELECT
    client.id,
    client.client_id,
    client.sub_client_id,
    append.name,
    COALESCE(client_append.individual_threshold, append.individual_threshold) individual_threshold,
    COALESCE(client_append.resident_threshold, append.resident_threshold) resident_threshold,
    COALESCE(client_append.household_threshold, append.household_threshold) household_threshold,
    COALESCE(client_append.address_threshold, append.address_threshold) address_threshold,
    COALESCE(client_append.last_name_threshold, append.last_name_threshold) last_name_threshold,
    COALESCE(client_append.date_of_birth_range_years, append.date_of_birth_range_years) date_of_birth_range_years,
    COALESCE(client_append.enable_fuzzy_gender_filter, append.enable_fuzzy_gender_filter) enable_fuzzy_gender_filter,
    COALESCE(client_append.suppress_state_do_not_call, append.suppress_state_do_not_call) suppress_state_do_not_call,
    COALESCE(client_append.suppress_federal_do_not_call, append.suppress_federal_do_not_call) suppress_federal_do_not_call,
    COALESCE(client_append.enabled, append.enabled) enabled,
    COALESCE(client_append.check_do_not_call, append.check_do_not_call) check_do_not_call,
    COALESCE(client_append.return_do_not_call_phone, append.return_do_not_call_phone) return_do_not_call_phone
FROM ${schemaName}.append
         LEFT JOIN ${schemaName}.client_append
                   ON client_append.append_id = append.id
         LEFT JOIN ${schemaName}.client
                   ON client.id = client_append.client_id OR append.enabled
ORDER BY client.client_id;