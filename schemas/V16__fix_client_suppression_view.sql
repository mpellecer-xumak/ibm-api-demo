DROP VIEW IF EXISTS ${schemaName}.client_suppression_view;
CREATE OR REPLACE VIEW ${schemaName}.client_suppression_view
AS
SELECT
    client.id,
    client.client_id,
    client.sub_client_id,
    suppression.name,
    COALESCE(client_suppression.resident_threshold, suppression.resident_threshold) resident_threshold,
    COALESCE(client_suppression.household_threshold, suppression.household_threshold) household_threshold,
    COALESCE(client_suppression.address_threshold, suppression.address_threshold) address_threshold,
    COALESCE(client_suppression.date_of_birth_range_years, suppression.date_of_birth_range_years) date_of_birth_range_years,
    COALESCE(client_suppression.enabled, suppression.enabled) enabled,
    COALESCE(client_suppression.enable_fuzzy_gender_filter, suppression.enable_fuzzy_gender_filter) enable_fuzzy_gender_filter
FROM ${schemaName}.suppression
         LEFT JOIN ${schemaName}.client_suppression
                   ON client_suppression.suppression_id = suppression.id
         LEFT JOIN ${schemaName}.client
                   ON client.id = client_suppression.client_id
ORDER BY client.client_id;
