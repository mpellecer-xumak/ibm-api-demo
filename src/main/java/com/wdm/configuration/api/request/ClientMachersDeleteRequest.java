package com.wdm.configuration.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientMachersDeleteRequest {
    @NotBlank(message = "hierarchyName value is required")
    private String hierarchyName;
    @NotBlank(message = "matcherName value is required")
    private String matcherName;
}
