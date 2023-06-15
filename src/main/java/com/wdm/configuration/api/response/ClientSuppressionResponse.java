package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.Suppression;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientSuppressionResponse extends ClientResponse {
    private List<Suppression> suppressions;
}
