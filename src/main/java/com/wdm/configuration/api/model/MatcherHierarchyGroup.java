package com.wdm.configuration.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatcherHierarchyGroup {
    @JsonIgnore
    private ClientConfig clientConfig;
    @NonNull
    private HierarchyEnum hierarchy;
    @NonNull
    private List<Matcher> matcherDetails;
}
