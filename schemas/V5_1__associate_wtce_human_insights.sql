-- Bundles
INSERT INTO ${schemaName}.bundle(name) VALUES ('wtce-batch'); -- 3
INSERT INTO ${schemaName}.bundle(name) VALUES ('bundle1'); -- 4
INSERT INTO ${schemaName}.bundle(name) VALUES ('bundle2'); -- 5
INSERT INTO ${schemaName}.bundle(name) VALUES ('bundle3'); -- 6

-- additional attributes
INSERT INTO ${schemaName}.attribute(name) VALUES ('ATTR4'); -- 4
INSERT INTO ${schemaName}.attribute(name) VALUES ('ATTR5'); -- 5
INSERT INTO ${schemaName}.attribute(name) VALUES ('ATTR6'); -- 6

-- wtd-batch bundle attribute
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (3, 4);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (3, 5);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (3, 6);

-- bundle1 bundle attributes
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (4, 4);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (4, 5);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (4, 6);

-- bundle2 bundle attributes
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (5, 4);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (5, 5);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (5, 6);

-- bundle3 bundle attributes
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (6, 1);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (6, 2);
INSERT INTO ${schemaName}.bundle_attribute(bundle_id, attribute_id) VALUES (6, 3);


-- wtce human_insights_bundle
INSERT INTO ${schemaName}.human_insights_bundle(client_id, bundle_id) VALUES (2, 3);
INSERT INTO ${schemaName}.human_insights_bundle(client_id, bundle_id) VALUES (2, 4);
INSERT INTO ${schemaName}.human_insights_bundle(client_id, bundle_id) VALUES (2, 5);
INSERT INTO ${schemaName}.human_insights_bundle(client_id, bundle_id) VALUES (2, 6);