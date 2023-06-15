package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "identity_resolution")
@SuppressWarnings("PMD.TooManyFields")
public class DbIdentityResolution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int matchLimit;
    @Column(nullable = false)
    private int lastNameThreshold;
    @Column(nullable = false)
    private int residentThreshold;
    @Column(nullable = false)
    private int individualThreshold;
    @Column(nullable = false)
    private int householdThreshold;
    @Column(nullable = false)
    private int addressThreshold;
    @Column(nullable = false)
    private int dateOfBirthRangeYears;
    @Column(nullable = false)
    private int insightBatchLimit;
    @Column(nullable = false)
    private int identityBatchLimit;
    @Column(nullable = false)
    private boolean stdAddressEnabled;
    @Column(nullable = false)
    private boolean useGraphId;
    @Column(nullable = false)
    private boolean enableKeyring;
    @Column(nullable = false)
    private boolean enableFuzzyGenderFilter;
    @Column(nullable = false)
    private boolean dpvStatusEnabled;
    @Column(nullable = false)
    private boolean enableAdminEndpoints;
}
