package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.VersionManagement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeploymentsResponse {
    private List<VersionManagement> deployments;
}
