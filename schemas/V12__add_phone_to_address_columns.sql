ALTER TABLE ${schemaName}.append
    ADD COLUMN enable_individual_landline BOOLEAN;
ALTER TABLE ${schemaName}.append
    ADD COLUMN enable_household_landline BOOLEAN;
ALTER TABLE ${schemaName}.append
    ADD COLUMN enable_contact_point_landline BOOLEAN;
ALTER TABLE ${schemaName}.append
    ADD COLUMN enable_individual_wireless BOOLEAN;
ALTER TABLE ${schemaName}.append
    ADD COLUMN enable_household_wireless BOOLEAN;
ALTER TABLE ${schemaName}.append
    ADD COLUMN enable_contact_point_wireless BOOLEAN;

ALTER TABLE ${schemaName}.client_append
    ADD COLUMN enable_individual_landline BOOLEAN;
ALTER TABLE ${schemaName}.client_append
    ADD COLUMN enable_household_landline BOOLEAN;
ALTER TABLE ${schemaName}.client_append
    ADD COLUMN enable_contact_point_landline BOOLEAN;
ALTER TABLE ${schemaName}.client_append
    ADD COLUMN enable_individual_wireless BOOLEAN;
ALTER TABLE ${schemaName}.client_append
    ADD COLUMN enable_household_wireless BOOLEAN;
ALTER TABLE ${schemaName}.client_append
    ADD COLUMN enable_contact_point_wireless BOOLEAN;
