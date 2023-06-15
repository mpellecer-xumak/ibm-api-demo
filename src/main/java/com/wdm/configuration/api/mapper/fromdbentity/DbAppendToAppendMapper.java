package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.persistence.entity.DbAppend;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;


@Component
public class DbAppendToAppendMapper extends Mapper<DbAppend, Append> {
    
    protected DbAppendToAppendMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }
    
    @Override
    protected Class<Append> getTargetType() {
        return Append.class;
    }

}
