package com.wdm.configuration.api.response;

import com.wdm.configuration.api.model.Append;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppendsResponse {
    private List<Append> appends;
}
