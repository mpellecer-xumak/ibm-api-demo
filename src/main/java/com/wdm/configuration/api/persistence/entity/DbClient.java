package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "client")
public class DbClient {
    private static final String CLIENT_STR = "client";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "client_id", length = 256, updatable = false, nullable = false)
    private String clientId;

    @Column(name = "sub_client_id", length = 256, updatable = false)
    private String subClientId;

    @Column(length = 32, updatable = false, nullable = false)
    private String key;

    @Column
    private boolean enabled;

    @Column
    private String batchFileProcessingBundle;

    @OneToOne(mappedBy = CLIENT_STR, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private DbIdentityResolutionClientConfig clientConfig;

    @OneToMany(mappedBy = CLIENT_STR, fetch = FetchType.LAZY)
    private List<DbClientAggregate> clientAggregates;

    @OneToMany(mappedBy = CLIENT_STR, fetch = FetchType.LAZY)
    private List<DbClientMatcher> clientMatchers;

    @OneToMany(mappedBy = CLIENT_STR, fetch = FetchType.LAZY)
    private List<DbClientSuppression> clientSuppressions;

    @OneToMany(mappedBy = CLIENT_STR, fetch = FetchType.LAZY)
    private List<DbClientAppend> clientAppends;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "human_insights_bundle",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "bundle_id")
    )
    private List<DbBundle> humanInsightBundles;
}
