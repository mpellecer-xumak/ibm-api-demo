package com.wdm.configuration.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Suppression {
    @NotBlank
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    private boolean enabled;
    private Boolean enableFuzzyGenderFilter;
    private Integer residentThreshold;
    private Integer householdThreshold;
    private Integer dateOfBirthRangeYears;
    private Integer addressThreshold;

    public Suppression(final boolean enabled, Boolean enableFuzzyGenderFilter, final Integer residentThreshold, final Integer householdThreshold,
                       final Integer dateOfBirthRangeYears, final Integer addressThreshold) {
        this.enabled = enabled;
        this.enableFuzzyGenderFilter = enableFuzzyGenderFilter;
        this.residentThreshold = residentThreshold;
        this.householdThreshold = householdThreshold;
        this.dateOfBirthRangeYears = dateOfBirthRangeYears;
        this.addressThreshold = addressThreshold;
    }
}
