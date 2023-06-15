package com.wdm.configuration.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientConfig extends PlatformsConfig {
    private String clientId;
    private String subClientId;
    private String key;
    private boolean enabled;
    private String batchFileProcessingBundle;
    private List<Bundle> bundles = new ArrayList<>();
    private List<MatcherHierarchyGroup> matchers = new ArrayList<>();
}
