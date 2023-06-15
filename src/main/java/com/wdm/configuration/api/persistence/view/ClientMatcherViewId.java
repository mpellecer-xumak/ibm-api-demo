package com.wdm.configuration.api.persistence.view;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClientMatcherViewId implements Serializable {
    private static final long serialVersionUID = -4422460982959342599L;

    private int id;
    private String hierarchyName;
    private String matcherName;
}
