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
public class ClientCreationRequest {
    @NotBlank(message = "The property 'clientId' is required")
    private String clientId;
    private String subClientId;
    @NotBlank(message = "The property 'key' is required")
    private String key;
    private boolean enabled;
    private String batchFileProcessingBundle;
    private PlatformConfig config;
}
