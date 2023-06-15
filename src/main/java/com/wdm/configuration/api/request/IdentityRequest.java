package com.wdm.configuration.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("PMD.TooManyFields")
public class IdentityRequest {
    private Integer matchLimit;
    private Integer lastNameThreshold;
    private Integer residentThreshold;
    private Integer individualThreshold;
    private Integer householdThreshold;
    private Integer dateOfBirthRangeYears;
    private Integer insightsBatchLimit;
    private Integer identityBatchLimit;
    private Integer addressThreshold;
    private Boolean stdAddressEnabled;
    private Boolean useGraphId;
    private Boolean enableKeyring;
    private Boolean enableFuzzyGenderFilter;
    private Boolean dpvStatusEnabled;
    private Boolean enableAdminEndpoints;
}
