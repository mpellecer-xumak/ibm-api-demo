UPDATE ${schemaName}.append
SET enable_individual_landline = true, enable_household_landline = true, enable_contact_point_landline = true, enable_individual_wireless = true, enable_household_wireless = true, enable_contact_point_wireless = true
WHERE name = 'namePhoneToAddress';
