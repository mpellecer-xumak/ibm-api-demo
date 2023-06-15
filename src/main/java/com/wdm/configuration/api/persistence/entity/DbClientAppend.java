package com.wdm.configuration.api.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client_append")
public class DbClientAppend extends DbAppendBase {
    private static final long serialVersionUID = 928396196446208L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private DbClient client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private DbAppend append;
    
}
