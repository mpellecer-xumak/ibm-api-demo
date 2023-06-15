package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAggregatesResponse {
    private List<Aggregate<Bundle>> aggregates;
}

