package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbIdentityResolution;
import com.wdm.configuration.api.request.IdentityRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DbIdentityResolutionToIdentityResolutionMapper extends Mapper<DbIdentityResolution,IdentityRequest> {

    protected DbIdentityResolutionToIdentityResolutionMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public void configure() {
        modelMapper.typeMap(DbIdentityResolution.class, IdentityRequest.class)
                .addMapping(DbIdentityResolution::getInsightBatchLimit, IdentityRequest::setInsightsBatchLimit);
    }

    @Override
    protected Class<IdentityRequest> getTargetType() {
        return IdentityRequest.class;
    }
}
