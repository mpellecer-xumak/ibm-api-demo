package com.wdm.configuration.api.persistence.view;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClientSuppressionViewId implements Serializable {
    private static final long serialVersionUID = 3917541942281874086L;

    private int id;
    private String name;
}
