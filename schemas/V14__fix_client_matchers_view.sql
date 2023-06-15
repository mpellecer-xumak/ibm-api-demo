CREATE OR REPLACE VIEW ${schemaName}.client_matcher_view AS
SELECT
    C.id,
    C.client_id,
    C.sub_client_id,
    H.name hierarchy_name,
    M.name matcher_name,
    CM.enabled AS enabled,
    CM.match_level AS match_level,
    CM.index AS index
FROM ${schemaName}.client C
         INNER JOIN ${schemaName}.client_matcher CM ON CM.client_id = C.id AND CM.enabled
         INNER JOIN ${schemaName}.matcher M ON M.id = CM.matcher_id
         INNER JOIN ${schemaName}.hierarchy H ON H.id = CM.hierarchy_id
UNION
SELECT
    C.id,
    C.client_id,
    C.sub_client_id,
    H.name hierarchy_name,
    M.name matcher_name,
    PM.enabled AS enabled,
    PM.match_level AS match_level,
    PM.index AS index
FROM ${schemaName}.client C
         CROSS JOIN ${schemaName}.matcher M
         CROSS JOIN ${schemaName}.hierarchy H
         INNER JOIN ${schemaName}.platform_matcher PM ON PM.matcher_id = M.id AND PM.hierarchy_id = H.id AND PM.enabled
WHERE (SELECT COUNT(1) FROM ${schemaName}.client_matcher CM WHERE CM.client_id = C.id AND CM.hierarchy_id = H.id) = 0;
