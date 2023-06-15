package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hierarchy")
public class DbHierarchy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, updatable = false, nullable = false)
    private String name;

    @OneToMany(mappedBy = "hierarchy", fetch = FetchType.LAZY)
    private List<DbClientMatcher> clientMatchers;

    @OneToMany(mappedBy = "hierarchy", fetch = FetchType.LAZY)
    private List<DbPlatformMatcher> platformMatchers;
}
