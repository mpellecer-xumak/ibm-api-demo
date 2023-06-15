-- Default deployment versions
INSERT INTO ${schemaName}.deployment_version(name, color, date_modified) VALUES ('Datasource1', 'BLUE', now());
INSERT INTO ${schemaName}.deployment_version(name, color, date_modified) VALUES ('Datasource2', 'GREEN', now());
INSERT INTO ${schemaName}.deployment_version(name, color, date_modified) VALUES ('Suppression1', 'BLUE', now());
INSERT INTO ${schemaName}.deployment_version(name, color, date_modified) VALUES ('Suppression2', 'GREEN', now());
INSERT INTO ${schemaName}.deployment_version(name, color, date_modified) VALUES ('Aggregate1', 'BLUE', now());
INSERT INTO ${schemaName}.deployment_version(name, color, date_modified) VALUES ('Aggregate2', 'GREEN', now());

-- Default hierarchies
INSERT INTO ${schemaName}.hierarchy(name) VALUES ('IDENTITY');
INSERT INTO ${schemaName}.hierarchy(name) VALUES ('INSIGHT');

-- client 1
INSERT INTO ${schemaName}.client(client_id, "key") VALUES ('client1', '4125442A472D4B6150645367566B5970');

-- client 2
INSERT INTO ${schemaName}.client(client_id, "key") VALUES ('client2', '4D6251655468576D5A7134743777397A');

-- Enable platform default configuration
INSERT INTO ${schemaName}.identity_resolution(enable_keyring) VALUES (FALSE);

-- WTD client specific configuration 
INSERT INTO ${schemaName}.identity_resolution_client_config(client_id, enable_fuzzy_gender_filter, enable_keyring, enable_admin_endpoints, date_of_birth_range_years) VALUES (1, TRUE, TRUE, TRUE, 999);

-- WTD client specific configuration
INSERT INTO ${schemaName}.identity_resolution_client_config(client_id, enable_fuzzy_gender_filter, enable_keyring, enable_admin_endpoints, date_of_birth_range_years) VALUES (2, TRUE, TRUE, TRUE, 999);

-- Suppressions creation
INSERT INTO ${schemaName}.suppression(name) VALUES ('doNotCall');
INSERT INTO ${schemaName}.suppression(name, resident_threshold, household_threshold, date_of_birth_range_years) VALUES ('doNotMail', 92, 85, 999);
INSERT INTO ${schemaName}.suppression(name, resident_threshold, household_threshold, date_of_birth_range_years) VALUES ('bankruptcy', 92, 85, 999);
INSERT INTO ${schemaName}.suppression(name, resident_threshold, date_of_birth_range_years) VALUES ('deceased', 92, 999);
INSERT INTO ${schemaName}.suppression(name, resident_threshold, date_of_birth_range_years) VALUES ('deceasedProbable', 92, 999);
INSERT INTO ${schemaName}.suppression(name, address_threshold) VALUES ('nursingHome', 99);
INSERT INTO ${schemaName}.suppression(name, address_threshold) VALUES ('prison', 99);
INSERT INTO ${schemaName}.suppression(name, address_threshold) VALUES ('publicHousing', 99);
INSERT INTO ${schemaName}.suppression(name) VALUES ('wireless');

-- Client 1 client suppressions
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 1, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 2, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 3, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 4, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 5, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 6, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 7, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 8, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (1, 9, TRUE);

-- Client 2 client suppressions
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 1, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 2, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 3, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 4, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 5, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 6, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 7, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 8, TRUE);
INSERT INTO ${schemaName}.client_suppression(client_id, suppression_id, enabled) VALUES (2, 9, TRUE);

-- Aggregates
INSERT INTO ${schemaName}.aggregate (name) VALUES ('aggregate1');
INSERT INTO ${schemaName}.aggregate (name) VALUES ('aggregate2');
INSERT INTO ${schemaName}.aggregate (name) VALUES ('aggregate3');
INSERT INTO ${schemaName}.aggregate (name) VALUES ('aggregate4');

-- Client 1 client aggregates
INSERT INTO ${schemaName}.client_aggregate (client_id, aggregate_id, enabled) VALUES (1, 1, TRUE);
INSERT INTO ${schemaName}.client_aggregate (client_id, aggregate_id, enabled) VALUES (1, 2, TRUE);
INSERT INTO ${schemaName}.client_aggregate (client_id, aggregate_id, enabled) VALUES (1, 3, TRUE);
INSERT INTO ${schemaName}.client_aggregate (client_id, aggregate_id, enabled) VALUES (1, 4, TRUE);

-- Client 2 client aggregates
INSERT INTO ${schemaName}.client_aggregate (client_id, aggregate_id, enabled) VALUES (2, 1, TRUE);
INSERT INTO ${schemaName}.client_aggregate (client_id, aggregate_id, enabled) VALUES (2, 2, TRUE);
INSERT INTO ${schemaName}.client_aggregate (client_id, aggregate_id, enabled) VALUES (2, 3, TRUE);
INSERT INTO ${schemaName}.client_aggregate (client_id, aggregate_id, enabled) VALUES (2, 4, TRUE);

-- Bundles
INSERT INTO ${schemaName}.bundle(name) VALUES ('client1-batch'); -- 1
INSERT INTO ${schemaName}.bundle(name) VALUES ('suppresion1-test'); -- 2

-- attributes
INSERT INTO ${schemaName}.attribute(name) VALUES ('ATTR1'); -- 1
INSERT INTO ${schemaName}.attribute(name) VALUES ('ATTR2'); -- 2
INSERT INTO ${schemaName}.attribute(name) VALUES ('ATTR3'); -- 3

-- client1-batch bundle attribute
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (1, 1);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (1, 2);

-- suppresion1-test attributes
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (2, 1);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (2, 2);

--  human_insights_bundle
INSERT INTO ${schemaName}.human_insights_bundle(client_id, bundle_id) VALUES (1, 1);

--  client aggregate_bundle
INSERT INTO ${schemaName}.client_aggregate_bundle(client_aggregate_id, bundle_id) VALUES (1, 1);
INSERT INTO ${schemaName}.client_aggregate_bundle(client_aggregate_id, bundle_id) VALUES (2, 2);

-- matchers
INSERT INTO ${schemaName}.matcher(name) VALUES ('MATCHER1'); -- 1
INSERT INTO ${schemaName}.matcher(name) VALUES ('MATCHER2'); -- 2

-- platform_matchers
INSERT INTO ${schemaName}.platform_matcher(hierarchy_id, matcher_id, index, match_level, enabled) VALUES (1, 1, 1, 1, TRUE);
INSERT INTO ${schemaName}.platform_matcher(hierarchy_id, matcher_id, index, match_level, enabled) VALUES (1, 2, 2, 1, TRUE);

-- client 1 client matchers
INSERT INTO ${schemaName}.client_matcher(client_id, hierarchy_id, matcher_id, index, match_level, enabled) VALUES (1, 1, 1, 1, 1, TRUE);

-- client 2 client matchers
INSERT INTO ${schemaName}.client_matcher(client_id, hierarchy_id, matcher_id, index, match_level, enabled) VALUES (2, 2, 1, 1, 1, TRUE);

