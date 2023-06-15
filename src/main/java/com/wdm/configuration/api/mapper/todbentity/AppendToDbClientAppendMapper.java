package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbClientAppend;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.model.Append;

@Component
public class AppendToDbClientAppendMapper extends Mapper<Append, DbClientAppend> {

    protected AppendToDbClientAppendMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbClientAppend> getTargetType() {
        return DbClientAppend.class;
    }
    
}
