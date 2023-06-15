package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "client_aggregate")
public class DbClientAggregate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   
    @Column
    private boolean enabled;

    @Column
    private String batchFileProcessingBundle;

    @ManyToOne(fetch = FetchType.LAZY)
    private DbClient client;

    @ManyToOne(fetch = FetchType.LAZY)
    private DbAggregate aggregate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "client_aggregate_bundle",
            joinColumns = @JoinColumn(name = "client_aggregate_id"),
            inverseJoinColumns = @JoinColumn(name = "bundle_id")
    )
    private List<DbBundle> aggregateBundles;
}
