package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "client_matcher")
public class DbClientMatcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Integer matchLevel;

    @Column(nullable = false)
    private Integer index;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    private DbMatcher matcher;

    @ManyToOne(fetch = FetchType.LAZY)
    private DbHierarchy hierarchy;

    @ManyToOne(fetch = FetchType.LAZY)
    private DbClient client;
}
