package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.persistence.entity.DbSuppression;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Suppression;

@Component
public class DbSuppressionToSuppression extends Mapper<DbSuppression, Suppression> {

    protected DbSuppressionToSuppression(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Suppression> getTargetType() {
        return Suppression.class;
    }

}
