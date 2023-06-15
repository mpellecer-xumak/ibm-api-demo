package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bundle")
public class DbBundle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, updatable = false, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "bundle_attribute",
            joinColumns = @JoinColumn(name = "bundle_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private List<DbAttribute> attributes;

    @ManyToMany(mappedBy = "humanInsightBundles", fetch = FetchType.LAZY)
    private List<DbClient> humanInsightClients;

    @ManyToMany(mappedBy = "bundles", fetch = FetchType.LAZY)
    private List<DbAggregate> aggregateBundles;

    @ManyToMany(mappedBy = "aggregateBundles", fetch = FetchType.LAZY)
    private List<DbClientAggregate> clientAggregateBundles;
}
