package com.wdm.configuration.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("PMD.TooManyFields")
public class PlatformConfig {
    private Integer matchLevel;
    private Integer matchLimit;
    private Integer lastNameThreshold;
    private Integer residentThreshold;
    private Integer individualThreshold;
    private Integer householdThreshold;
    private Integer dateOfBirthRangeYears;
    private Integer identityBatchLimit;
    private Integer insightsBatchLimit;
    private Integer addressThreshold;
    private Boolean stdAddressEnabled;
    private Boolean useGraphId;
    private Boolean enableKeyring;
    private Boolean enableFuzzyGenderFilter;
    private Boolean enableAdminEndpoints;
    private Boolean dpvStatusEnabled;
}
