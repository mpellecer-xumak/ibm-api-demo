package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbClient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.request.ClientCreationRequest;

@Component
public class ClientToClientDbMapper extends Mapper<ClientCreationRequest, DbClient> {

    protected ClientToClientDbMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected void configure() {
        modelMapper.createTypeMap(ClientCreationRequest.class, DbClient.class)
                .addMappings(mapper -> mapper.skip(DbClient::setId))
                .addMappings(mapper -> mapper.skip(DbClient::setClientAggregates))
                .addMappings(mapper -> mapper.skip(DbClient::setClientAppends))
                .addMappings(mapper -> mapper.skip(DbClient::setClientConfig))
                .addMappings(mapper -> mapper.skip(DbClient::setClientMatchers))
                .addMappings(mapper -> mapper.skip(DbClient::setClientSuppressions));
    }

    @Override
    protected Class<DbClient> getTargetType() {
        return DbClient.class;
    }

}
