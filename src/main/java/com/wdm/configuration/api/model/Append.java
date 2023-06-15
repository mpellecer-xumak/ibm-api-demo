package com.wdm.configuration.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"PMD.ExcessiveParameterList", "PMD.TooManyFields"})
public class Append {
    @NotBlank
    private String name;
    private boolean enabled;
    @NotNull
    private Boolean suppressStateDoNotCall;
    @NotNull
    private Boolean suppressFederalDoNotCall;
    @NotNull
    private Boolean enableFuzzyGenderFilter;
    private Integer addressThreshold;
    private Integer residentThreshold;
    private Integer householdThreshold;
    private Integer individualThreshold;
    private Integer lastNameThreshold;
    private Integer dateOfBirthRangeYears;
    private Boolean checkDoNotCall;
    private Boolean returnDoNotCallPhone;
    private Boolean enableIndividualLandline;
    private Boolean enableHouseholdLandline;
    private Boolean enableContactPointLandline;
    private Boolean enableIndividualWireless;
    private Boolean enableHouseholdWireless;
    private Boolean enableContactPointWireless;

    public Append(boolean enabled, Boolean suppressStateDoNotCall, Boolean suppressFederalDoNotCall,
            Integer residentThreshold, Integer householdThreshold, Integer individualThreshold,
            Integer lastNameThreshold, Integer dateOfBirthRangeYears, Boolean checkDoNotCall,
            Boolean returnDoNotCallPhone, Boolean enableIndividualLandline, Boolean enableHouseholdLandline,
            Boolean enableContactPointLandline, Boolean enableIndividualWireless, Boolean enableHouseholdWireless,
            Boolean enableContactPointWireless) {
        this.enabled = enabled;
        this.suppressStateDoNotCall = suppressStateDoNotCall;
        this.suppressFederalDoNotCall = suppressFederalDoNotCall;
        this.residentThreshold = residentThreshold;
        this.householdThreshold = householdThreshold;
        this.individualThreshold = individualThreshold;
        this.lastNameThreshold = lastNameThreshold;
        this.dateOfBirthRangeYears = dateOfBirthRangeYears;
        this.checkDoNotCall = checkDoNotCall;
        this.returnDoNotCallPhone = returnDoNotCallPhone;
        this.enableIndividualLandline = enableIndividualLandline;
        this.enableHouseholdLandline = enableHouseholdLandline;
        this.enableContactPointLandline = enableContactPointLandline;
        this.enableIndividualWireless = enableIndividualWireless;
        this.enableHouseholdWireless = enableHouseholdWireless;
        this.enableContactPointWireless = enableContactPointWireless;
    }
}