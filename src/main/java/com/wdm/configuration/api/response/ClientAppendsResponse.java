package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.Append;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientAppendsResponse extends ClientResponse {
    private List<Append> appends;
}
