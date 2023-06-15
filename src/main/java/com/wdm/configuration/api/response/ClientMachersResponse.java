package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.MatcherHierarchyGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientMachersResponse extends ClientResponse {
    private List<MatcherHierarchyGroup> matchers;
}
