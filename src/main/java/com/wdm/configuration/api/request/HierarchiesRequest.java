package com.wdm.configuration.api.request;

import com.wdm.configuration.api.model.Hierarchy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HierarchiesRequest {
    private List<Hierarchy> hierarchies;
}
