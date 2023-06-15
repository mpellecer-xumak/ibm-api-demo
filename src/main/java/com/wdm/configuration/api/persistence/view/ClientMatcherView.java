package com.wdm.configuration.api.persistence.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Immutable
@IdClass(ClientMatcherViewId.class)
@Table(name = "client_matcher_view")
public class ClientMatcherView extends BaseView {
    private static final long serialVersionUID = 1254896475210360393L;

    @Id
    private int id;

    @Id
    @Column(name = "hierarchy_name")
    private String hierarchyName;

    @Id
    @Column(name = "matcher_name")
    private String matcherName;

    @Column(name = "match_level")
    private Integer matchLevel;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "index")
    private int index;
}
