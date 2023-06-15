package com.wdm.configuration.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Matcher {
    @NotBlank
    private String name;
    @NotBlank
    private String hierarchy;
    @NotNull
    private int matchLevel;
    @NotNull
    private boolean enabled;
    @NotNull
    private int index;

    public Matcher(final String name) {
        this.name = name;
    }

    public Matcher(final String name, final int matchLevel, final boolean enabled, final int index) {
        this.name = name;
        this.matchLevel = matchLevel;
        this.enabled = enabled;
        this.index = index;
    }
}
