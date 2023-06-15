package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbSuppression;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.model.Suppression;

@Component
public class SuppressionToDbSuppressionMapper extends Mapper<Suppression, DbSuppression> {

    protected SuppressionToDbSuppressionMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbSuppression> getTargetType() {
        return DbSuppression.class;
    }
    
}
