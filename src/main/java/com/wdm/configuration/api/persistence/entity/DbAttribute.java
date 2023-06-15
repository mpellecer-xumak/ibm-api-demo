package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "attribute")
public class DbAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, updatable = false, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "attributes", fetch = FetchType.LAZY)
    private List<DbBundle> bundles;

    public DbAttribute(final String name) {
        this.name = name;
    }
}
