package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "platform_matcher")
public class DbPlatformMatcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int matchLevel;

    @Column(nullable = false)
    private int index;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    private DbMatcher matcher;

    @ManyToOne(fetch = FetchType.EAGER)
    private DbHierarchy hierarchy;

}
