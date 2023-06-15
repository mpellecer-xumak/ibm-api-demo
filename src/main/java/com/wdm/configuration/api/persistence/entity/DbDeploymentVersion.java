package com.wdm.configuration.api.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "deployment_version")
public class DbDeploymentVersion {
    @Id
    private String name;

    @Column(length = 10, nullable = false)
    private String color;

    @UpdateTimestamp
    private LocalDateTime dateModified;

    @Column(length = 256)
    private String build;
}
