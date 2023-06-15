package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.PlatformMatcher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
public class PlatformMatchersResponse {
    private List<PlatformMatcher> matchers;
}
