package com.wdm.configuration.api.persistence.view;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class IdentityResolutionClientConfigViewId implements Serializable {
    private static final long serialVersionUID = 2247593042928564086L;

    private String clientId;
    private String subClientId;
}
