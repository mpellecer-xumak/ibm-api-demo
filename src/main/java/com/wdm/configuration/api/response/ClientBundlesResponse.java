package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.Bundle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientBundlesResponse extends ClientResponse {
    private List<Bundle> bundles;
}
