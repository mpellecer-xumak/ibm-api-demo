DROP VIEW IF EXISTS ${schemaName}.identity_resolution_client_config_view;
CREATE OR REPLACE VIEW ${schemaName}.identity_resolution_client_config_view
AS
    SELECT
        client.id,
        client.client_id,
        client.sub_client_id,
        client."key",
        client.enabled,
        COALESCE(client_config.match_limit, ir_default.match_limit) match_limit,
        COALESCE(client_config.last_name_threshold, ir_default.last_name_threshold) last_name_threshold,
        COALESCE(client_config.resident_threshold, ir_default.resident_threshold) resident_threshold,
        COALESCE(client_config.individual_threshold, ir_default.individual_threshold) individual_threshold,
        COALESCE(client_config.household_threshold, ir_default.household_threshold) household_threshold,
        COALESCE(client_config.address_threshold, ir_default.address_threshold) address_threshold,
        COALESCE(client_config.date_of_birth_range_years, ir_default.date_of_birth_range_years) date_of_birth_range_years,
        COALESCE(client_config.insight_batch_limit, ir_default.insight_batch_limit) insight_batch_limit,
        COALESCE(client_config.identity_batch_limit, ir_default.identity_batch_limit) identity_batch_limit,
        COALESCE(client_config.std_address_enabled, ir_default.std_address_enabled) std_address_enabled,
        COALESCE(client_config.use_graph_id, ir_default.use_graph_id) use_graph_id,
        COALESCE(client_config.enable_keyring, ir_default.enable_keyring) enable_keyring,
        COALESCE(client_config.enable_fuzzy_gender_filter, ir_default.enable_fuzzy_gender_filter) enable_fuzzy_gender_filter,
        COALESCE(client_config.enable_admin_endpoints, ir_default.enable_admin_endpoints) enable_admin_endpoints,
        COALESCE(client_config.dpv_status_enabled, ir_default.dpv_status_enabled) dpv_status_enabled
    FROM ${schemaName}.client client
             LEFT JOIN ${schemaName}.identity_resolution_client_config client_config
                       ON client.id = client_config.client_id
             LEFT JOIN ${schemaName}.identity_resolution ir_default
                       ON client.client_id IS NOT NULL;

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
        COALESCE(client_suppression.enabled, suppression.enabled) enabled
    FROM ${schemaName}.suppression
             LEFT JOIN ${schemaName}.client_suppression
                       ON client_suppression.suppression_id = suppression.id
             LEFT JOIN ${schemaName}.client
                       ON client.id = client_suppression.client_id OR suppression.enabled
    ORDER BY client.client_id;

DROP VIEW IF EXISTS ${schemaName}.client_matcher_view;
CREATE OR REPLACE VIEW ${schemaName}.client_matcher_view
AS
    SELECT
        client.id,
        client.client_id,
        client.sub_client_id,
        hierarchy.name hierarchy_name,
        matcher.name matcher_name,
        COALESCE(client_matcher.enabled, platform_matcher.enabled) AS enabled,
        COALESCE(client_matcher.match_level, platform_matcher.match_level) AS match_level,
        COALESCE(client_matcher.index, platform_matcher.index) AS index
    FROM ${schemaName}.client
             CROSS JOIN ${schemaName}.matcher
             CROSS JOIN ${schemaName}.hierarchy
             LEFT JOIN ${schemaName}.client_matcher
                       ON client_matcher.client_id = client.id
                           AND client_matcher.hierarchy_id = hierarchy.id
                           AND client_matcher.matcher_id = matcher.id
                           AND client_matcher.enabled
             LEFT JOIN ${schemaName}.platform_matcher
                       ON client_matcher.client_id IS NULL
                           AND platform_matcher.matcher_id = matcher.id
                           AND platform_matcher.hierarchy_id = hierarchy.id
                           AND platform_matcher.enabled
    WHERE client_matcher.client_id IS NOT NULL
       OR platform_matcher.id IS NOT NULL
    ORDER BY client.client_id;

DROP VIEW IF EXISTS ${schemaName}.client_aggregate_view;
CREATE OR REPLACE VIEW ${schemaName}.client_aggregate_view
AS
    SELECT
        client.id,
        client.client_id,
        client.sub_client_id,
        aggregate.name aggregate_name,
        aggregate.enabled,
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
             INNER JOIN ${schemaName}.bundle
                        ON bundle.id = aggregate_bundle.bundle_id
                            OR bundle.id = client_aggregate_bundle.bundle_id
             INNER JOIN ${schemaName}.bundle_attribute
                        ON bundle_attribute.bundle_id = bundle.id
             INNER JOIN ${schemaName}.attribute
                        ON attribute.id = bundle_attribute.attribute_id
    GROUP BY client.id, client.client_id, client.sub_client_id, aggregate.name, aggregate.enabled, bundle.name, attribute.name
    ORDER BY client.client_id;
