package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbPlatformMatcher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.model.Matcher;

@Component
public class MatcherToDbPlatformMatcherMapper extends Mapper<Matcher, DbPlatformMatcher> {

    protected MatcherToDbPlatformMatcherMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbPlatformMatcher> getTargetType() {
        return DbPlatformMatcher.class;
    }

    @Override
    protected void configure() {
        modelMapper.createTypeMap(Matcher.class, DbPlatformMatcher.class)
                .addMappings(mapper -> mapper.skip(DbPlatformMatcher::setMatcher))
                .addMappings(mapper -> mapper.skip(DbPlatformMatcher::setHierarchy));
    }

}
