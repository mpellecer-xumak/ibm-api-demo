package com.wdm.configuration.api.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bundle {
    @NotBlank
    private String name;
    @JsonIgnore
    private BundleType bundleType;
    @NotEmpty
    private List<Attribute> attributes;
    private Long bundleId;
    private Set<ClientConfig> clientConfigs;
    private Set<BundleAttribute> bundleAttributes;

    public Bundle(final String bundleName) {
        this.name = bundleName;
        this.attributes = new ArrayList<>();
    }

    public Bundle(String name, List<Attribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }
}
