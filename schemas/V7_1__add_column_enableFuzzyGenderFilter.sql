ALTER TABLE ${schemaName}.suppression
    ADD COLUMN enable_fuzzy_gender_filter BOOLEAN;

ALTER TABLE ${schemaName}.client_suppression
    ADD COLUMN enable_fuzzy_gender_filter BOOLEAN;

UPDATE ${schemaName}.suppression SET enable_fuzzy_gender_filter = false where name = 'deceased';
UPDATE ${schemaName}.suppression SET enable_fuzzy_gender_filter = false where name = 'deceasedProbable';