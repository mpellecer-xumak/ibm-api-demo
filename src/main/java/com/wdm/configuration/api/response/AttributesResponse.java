package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributesResponse {
    private List<Attribute> attributes;
}
