package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.ClientConfig;
import com.wdm.configuration.api.persistence.view.IdentityResolutionClientConfigView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DbClientConfigToClientConfigMapper extends Mapper<IdentityResolutionClientConfigView, ClientConfig> {
    public DbClientConfigToClientConfigMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public void configure() {
        modelMapper.typeMap(IdentityResolutionClientConfigView.class, ClientConfig.class)
                .addMapping(IdentityResolutionClientConfigView::getInsightBatchLimit, ClientConfig::setInsightsBatchLimit)
                .addMapping(IdentityResolutionClientConfigView::getIdentityBatchLimit, ClientConfig::setIdentityBatchLimit);
    }

    @Override
    public Class<ClientConfig> getTargetType() {
        return ClientConfig.class;
    }
}
