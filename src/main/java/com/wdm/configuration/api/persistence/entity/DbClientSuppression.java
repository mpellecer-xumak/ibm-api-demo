package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "client_suppression")
public class DbClientSuppression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer residentThreshold;

    private Integer householdThreshold;

    private Integer addressThreshold;

    private Integer dateOfBirthRangeYears;

    private boolean enabled;

    private Boolean enableFuzzyGenderFilter;

    @ManyToOne(fetch = FetchType.LAZY)
    private DbClient client;

    @ManyToOne(fetch = FetchType.LAZY)
    private DbSuppression suppression;

}
