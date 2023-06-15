package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbAppend;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.model.Append;

@Component
public class AppendToDbAppendMapper extends Mapper<Append, DbAppend> {

    protected AppendToDbAppendMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbAppend> getTargetType() {
        return DbAppend.class;
    }
    
}
