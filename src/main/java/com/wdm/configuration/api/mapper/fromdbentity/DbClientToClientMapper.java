package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.ClientConfig;
import com.wdm.configuration.api.persistence.entity.DbClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DbClientToClientMapper extends Mapper<DbClient, ClientConfig> {
    protected DbClientToClientMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<ClientConfig> getTargetType() {
        return ClientConfig.class;
    }
}
