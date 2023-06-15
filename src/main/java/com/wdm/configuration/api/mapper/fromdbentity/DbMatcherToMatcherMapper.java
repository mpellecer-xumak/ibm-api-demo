package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DbMatcherToMatcherMapper extends Mapper<DbMatcher, SimpleName> {

    protected DbMatcherToMatcherMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<SimpleName> getTargetType() {
        return SimpleName.class;
    }

}
