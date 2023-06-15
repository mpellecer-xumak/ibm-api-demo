package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "matcher")
public class DbMatcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50, updatable = false, nullable = false)
    private String name;

    @OneToMany(mappedBy = "matcher", fetch = FetchType.LAZY)
    private List<DbClientMatcher> clientMatchers;

    @OneToMany(mappedBy = "matcher", fetch = FetchType.LAZY)
    private List<DbPlatformMatcher> platformMatchers;

}
