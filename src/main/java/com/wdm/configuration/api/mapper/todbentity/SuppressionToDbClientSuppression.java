package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.persistence.entity.DbClientSuppression;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SuppressionToDbClientSuppression extends Mapper<Suppression, DbClientSuppression> {
    protected SuppressionToDbClientSuppression(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbClientSuppression> getTargetType() {
        return DbClientSuppression.class;
    }
}
