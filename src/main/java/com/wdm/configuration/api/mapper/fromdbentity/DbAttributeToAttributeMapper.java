package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.persistence.entity.DbAttribute;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DbAttributeToAttributeMapper extends Mapper<DbAttribute, Attribute> {
    public DbAttributeToAttributeMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Class<Attribute> getTargetType() {
        return Attribute.class;
    }
}
