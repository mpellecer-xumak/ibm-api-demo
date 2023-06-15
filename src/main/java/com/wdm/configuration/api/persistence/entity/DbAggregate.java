package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "aggregate")
public class DbAggregate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, updatable = false, nullable = false)
    private String name;
    
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "aggregate_bundle",
            joinColumns = @JoinColumn(name = "aggregate_id"),
            inverseJoinColumns = @JoinColumn(name = "bundle_id")
    )
    private List<DbBundle> bundles;

    @OneToMany(mappedBy = "aggregate", fetch = FetchType.LAZY)
    private List<DbClientAggregate> clients;
}
