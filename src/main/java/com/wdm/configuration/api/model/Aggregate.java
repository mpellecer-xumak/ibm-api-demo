package com.wdm.configuration.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aggregate<T> {
    @NotEmpty
    private String name;
    private boolean enabled;
    private String batchFileProcessingBundle;
    private List<T> bundles = new ArrayList<>();

    public Aggregate(final boolean enabled, final List<T> bundles) {
        this.enabled = enabled;
        this.bundles = bundles;
    }

    public Aggregate(final String name, final boolean enabled, final List<T> bundles) {
        this.name = name;
        this.enabled = enabled;
        this.bundles = bundles;
    }

    public Aggregate(String name, boolean enabled, String batchFileProcessingBundle) {
        this.name = name;
        this.enabled = enabled;
        this.batchFileProcessingBundle = batchFileProcessingBundle;
    }

}
