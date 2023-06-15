package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.persistence.view.ClientAppendView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DbClientAppendViewToAppendMapper extends Mapper<ClientAppendView, Append> {

    protected DbClientAppendViewToAppendMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Append> getTargetType() {
        return Append.class;
    }

}
