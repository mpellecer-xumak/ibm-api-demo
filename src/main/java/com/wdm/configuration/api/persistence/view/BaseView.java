package com.wdm.configuration.api.persistence.view;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@SuppressWarnings("PMD")
@Setter
@Getter
@MappedSuperclass
public abstract class BaseView implements Serializable {
    private static final long serialVersionUID = 3690562727163170393L;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "sub_client_id")
    private String subClientId;

}
