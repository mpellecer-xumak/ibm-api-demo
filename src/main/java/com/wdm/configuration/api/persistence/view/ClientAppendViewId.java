package com.wdm.configuration.api.persistence.view;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClientAppendViewId implements Serializable {
    private static final long serialVersionUID = 6063554282387116328L;

    private int id;
    private String name;
}
