package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.wdm.configuration.api.model.Matcher;

@Component
public class MatcherToDbMatcherMapper extends Mapper<SimpleName, DbMatcher> {

    protected MatcherToDbMatcherMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbMatcher> getTargetType() {
        return DbMatcher.class;
    }

    @Override
    protected void configure() {
        modelMapper.createTypeMap(Matcher.class, DbMatcher.class)
                .addMappings(mapper -> mapper.skip(DbMatcher::setClientMatchers))
                .addMappings(mapper -> mapper.skip(DbMatcher::setPlatformMatchers));
    }

}
