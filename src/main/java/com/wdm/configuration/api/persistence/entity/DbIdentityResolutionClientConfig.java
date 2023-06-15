package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "identity_resolution_client_config")
@SuppressWarnings("PMD.TooManyFields")
public class DbIdentityResolutionClientConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer matchLimit;

    private Integer lastNameThreshold;

    private Integer residentThreshold;

    private Integer individualThreshold;

    private Integer householdThreshold;

    private Integer addressThreshold;

    private Integer dateOfBirthRangeYears;

    private Integer identityBatchLimit;

    private Integer insightBatchLimit;

    private Boolean stdAddressEnabled;

    private Boolean useGraphId;

    private Boolean enableKeyring;

    private Boolean enableFuzzyGenderFilter;

    private Boolean dpvStatusEnabled;

    private Boolean enableAdminEndpoints;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private DbClient client;
}
