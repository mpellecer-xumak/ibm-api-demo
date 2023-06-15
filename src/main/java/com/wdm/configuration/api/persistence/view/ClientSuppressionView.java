package com.wdm.configuration.api.persistence.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Immutable
@IdClass(ClientSuppressionViewId.class)
@Table(name = "client_suppression_view")
public class ClientSuppressionView extends BaseView {
    private static final long serialVersionUID = -3690874595487521698L;

    @Id
    private int id;

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "resident_threshold")
    private Integer residentThreshold;

    @Column(name = "household_threshold")
    private Integer householdThreshold;

    @Column(name = "address_threshold")
    private Integer addressThreshold;

    @Column(name = "date_of_birth_range_years")
    private Integer dateOfBirthRangeYears;

    @Column
    private boolean enabled;

    @Column
    private Boolean enableFuzzyGenderFilter;
}
