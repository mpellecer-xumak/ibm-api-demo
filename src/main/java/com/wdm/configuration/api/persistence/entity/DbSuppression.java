package com.wdm.configuration.api.persistence.entity;

import javax.persistence.*;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "suppression")
public class DbSuppression {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32)
    private String name;
    
    private Integer residentThreshold;
    
    private Integer householdThreshold;
    
    private Integer addressThreshold;
    
    private Integer dateOfBirthRangeYears;
    
    private boolean enabled;

    private Boolean enableFuzzyGenderFilter;

    @OneToMany(mappedBy = "suppression", fetch = FetchType.LAZY)
    private List<DbClientSuppression> clientSuppressions;
}
