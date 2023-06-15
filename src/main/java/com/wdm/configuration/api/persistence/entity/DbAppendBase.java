package com.wdm.configuration.api.persistence.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@SuppressWarnings("PMD.TooManyFields")
public class DbAppendBase implements Serializable  {
    private static final long serialVersionUID = 2846602553196544L;

    private Integer individualThreshold;
    
    private Integer residentThreshold;
    
    private Integer householdThreshold;
    
    private Integer addressThreshold;
    
    private Integer lastNameThreshold;
    
    private Integer dateOfBirthRangeYears;
    
    private Boolean enableFuzzyGenderFilter;
    
    private Boolean suppressStateDoNotCall;
    
    private Boolean suppressFederalDoNotCall;
    
    private Boolean enabled;
    
    private Boolean checkDoNotCall;
    
    private Boolean returnDoNotCallPhone;

    private Boolean enableIndividualLandline;

    private Boolean enableHouseholdLandline;

    private Boolean enableContactPointLandline;

    private Boolean enableIndividualWireless;

    private Boolean enableHouseholdWireless;

    private Boolean enableContactPointWireless;
}
