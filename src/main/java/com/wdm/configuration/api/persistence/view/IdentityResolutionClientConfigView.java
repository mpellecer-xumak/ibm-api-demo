package com.wdm.configuration.api.persistence.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Immutable
@SuppressWarnings("PMD")
@Table(name = "identity_resolution_client_config_view")
public class IdentityResolutionClientConfigView extends BaseView {
    private static final long serialVersionUID = 3654821987563218749L;

    @Id
    private int id;

    @Column
    private String key;

    @Column
    private int matchLimit;

    @Column
    private String batchFileProcessingBundle;

    @Column
    private int lastNameThreshold;

    @Column
    private int residentThreshold;

    @Column
    private int individualThreshold;

    @Column
    private int householdThreshold;

    @Column
    private int addressThreshold;

    @Column
    private int dateOfBirthRangeYears;

    @Column
    private int insightBatchLimit;

    @Column
    private int identityBatchLimit;

    @Column
    private boolean enabled;

    @Column
    private boolean stdAddressEnabled;

    @Column
    private boolean useGraphId;

    @Column
    private boolean enableKeyring;

    @Column
    private boolean enableFuzzyGenderFilter;

    @Column
    private boolean enableAdminEndpoints;

    @Column
    private boolean dpvStatusEnabled;
}
