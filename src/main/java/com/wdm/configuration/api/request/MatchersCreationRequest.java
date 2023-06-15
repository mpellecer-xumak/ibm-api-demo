package com.wdm.configuration.api.request;

import com.wdm.configuration.api.model.SimpleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchersCreationRequest {
    @NotEmpty
    private List<SimpleName> matchers;

}
