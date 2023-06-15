package com.wdm.configuration.api.persistence.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@IdClass(ClientAppendViewId.class)
@Table(name = "client_append_view")
@SuppressWarnings("PMD.TooManyFields")
public class ClientAppendView extends BaseView {
    private static final long serialVersionUID = 3058937964662078385L;

    @Id
    private int id;

    @Id
    @Column
    private String name;

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
