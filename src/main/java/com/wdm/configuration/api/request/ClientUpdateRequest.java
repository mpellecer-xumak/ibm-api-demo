package com.wdm.configuration.api.request;

import com.wdm.configuration.api.model.PlatformConfig;
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
public class ClientUpdateRequest {
    private boolean enabled;
    @NotBlank(message = "The batch file processing bundle is required")
    private String batchFileProcessingBundle;
    private PlatformConfig config;
}
