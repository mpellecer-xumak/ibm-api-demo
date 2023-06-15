package com.wdm.configuration.api.persistence.view;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClientAggregateViewId implements Serializable {
    private static final long serialVersionUID = -3690562727163170393L;

    private String id;
    private String aggregateName;
}
