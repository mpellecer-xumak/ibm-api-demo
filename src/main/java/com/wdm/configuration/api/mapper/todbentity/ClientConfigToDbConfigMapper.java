package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbIdentityResolutionClientConfig;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.model.PlatformConfig;

@Component
public class ClientConfigToDbConfigMapper extends Mapper<PlatformConfig, DbIdentityResolutionClientConfig> {

    protected ClientConfigToDbConfigMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbIdentityResolutionClientConfig> getTargetType() {
        return DbIdentityResolutionClientConfig.class;
    }

    @Override
    protected void configure() {
        modelMapper.createTypeMap(PlatformConfig.class, DbIdentityResolutionClientConfig.class)
                .addMapping(PlatformConfig::getInsightsBatchLimit,
                        DbIdentityResolutionClientConfig::setInsightBatchLimit);
    }
}
