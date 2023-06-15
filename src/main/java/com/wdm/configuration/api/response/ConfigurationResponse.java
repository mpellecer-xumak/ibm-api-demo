package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.ClientConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponse {
    private ClientConfig config;
}
