package com.wdm.configuration.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("PMD.TooManyFields")
public class PlatformsConfig {
    private Integer matchLevel;
    private Integer matchLimit;
    private Integer lastNameThreshold;
    private Integer residentThreshold;
    private Integer individualThreshold;
    private Integer householdThreshold;
    private Integer dateOfBirthRangeYears;
    private Integer identityBatchLimit;
    private Integer insightsBatchLimit;
    private Boolean stdAddressEnabled;
    private Integer addressThreshold;
    private Boolean useGraphId;
    private Boolean enableKeyring;
    private Boolean enableFuzzyGenderFilter;
    private Boolean enableAdminEndpoints;
    private Boolean dpvStatusEnabled;
    private Map<String, Aggregate<Bundle>> aggregates;
    private Map<String, Suppression> suppressions;
    private Map<String, Append> appends;
}
