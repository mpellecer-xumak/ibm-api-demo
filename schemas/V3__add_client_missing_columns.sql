ALTER TABLE ${schemaName}.identity_resolution_client_config
    ADD COLUMN identity_batch_limit INT;
ALTER TABLE ${schemaName}.identity_resolution
    ADD COLUMN identity_batch_limit INT DEFAULT 1000;

ALTER TABLE ${schemaName}.identity_resolution_client_config
    ADD COLUMN enable_admin_endpoints BOOLEAN;
ALTER TABLE ${schemaName}.identity_resolution
    ADD COLUMN enable_admin_endpoints BOOLEAN DEFAULT FALSE;