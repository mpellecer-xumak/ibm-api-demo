package com.wdm.configuration.api.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "append")
public class DbAppend extends DbAppendBase {
    private static final long serialVersionUID = 2746565712150528L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(length = 32, updatable = false, nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "append", fetch = FetchType.LAZY)
    private List<DbClientAppend> clientAppends;
}
