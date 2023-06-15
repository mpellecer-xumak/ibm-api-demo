package com.wdm.configuration.api.response;

import java.util.List;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericListResponse<T> {
    private List<T> response;
}
