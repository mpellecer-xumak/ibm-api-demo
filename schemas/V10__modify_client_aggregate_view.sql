DROP VIEW IF EXISTS ${schemaName}.client_aggregate_view;
CREATE OR REPLACE VIEW ${schemaName}.client_aggregate_view
AS
SELECT
    CONCAT(client.id,bundle.name,attribute.name) id,
    client.client_id,
    client.sub_client_id,
    aggregate.name aggregate_name,
    COALESCE(client_aggregate.enabled, aggregate.enabled) enabled,
    bundle.name bundle_name,
    attribute.name attribute_name
FROM ${schemaName}.aggregate
         CROSS JOIN ${schemaName}.client
         LEFT JOIN ${schemaName}.client_aggregate
                   ON client_aggregate.client_id = client.id
                       AND client_aggregate.aggregate_id = aggregate.id
         LEFT JOIN ${schemaName}.client_aggregate_bundle
                   ON client_aggregate_bundle.client_aggregate_id = client_aggregate.id
                       AND client_aggregate.enabled
         LEFT JOIN ${schemaName}.aggregate_bundle
                   ON aggregate_bundle.aggregate_id = aggregate.id
                       AND (aggregate.enabled OR client_aggregate.enabled)
         LEFT JOIN ${schemaName}.bundle
                   ON bundle.id = aggregate_bundle.bundle_id
                       OR bundle.id = client_aggregate_bundle.bundle_id
         LEFT JOIN ${schemaName}.bundle_attribute
                   ON bundle_attribute.bundle_id = bundle.id
         LEFT JOIN ${schemaName}.attribute
                   ON attribute.id = bundle_attribute.attribute_id
WHERE aggregate.enabled = TRUE
   OR client_aggregate.enabled = TRUE
GROUP BY client.id, client.client_id, client.sub_client_id, aggregate.name, client_aggregate.enabled, aggregate.enabled, bundle.name, attribute.name
ORDER BY client.client_id;