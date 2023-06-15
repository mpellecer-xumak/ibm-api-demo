package com.wdm.configuration.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentStateResponse {
    private String state;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldState;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String entity;
}
