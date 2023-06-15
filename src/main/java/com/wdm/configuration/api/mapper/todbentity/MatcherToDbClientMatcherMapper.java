package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbClientMatcher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.model.Matcher;

@Component
public class MatcherToDbClientMatcherMapper extends Mapper<Matcher, DbClientMatcher> {

    protected MatcherToDbClientMatcherMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbClientMatcher> getTargetType() {
        return DbClientMatcher.class;
    }

    @Override
    protected void configure() {
        modelMapper.createTypeMap(Matcher.class, DbClientMatcher.class)
                .addMappings(mapper -> mapper.skip(DbClientMatcher::setClient))
                .addMappings(mapper -> mapper.skip(DbClientMatcher::setHierarchy))
                .addMappings(mapper -> mapper.skip(DbClientMatcher::setMatcher));
    }

}
