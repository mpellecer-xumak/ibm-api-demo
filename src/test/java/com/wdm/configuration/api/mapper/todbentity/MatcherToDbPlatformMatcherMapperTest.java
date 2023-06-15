package com.wdm.configuration.api.mapper.todbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.persistence.entity.DbPlatformMatcher;

public class MatcherToDbPlatformMatcherMapperTest extends BaseMapperTest<Matcher, DbPlatformMatcher> {
    private static final String MATCHER_NAME = "myMatcher";
    private static final String HIERARCHY_NAME = "myHierarchy";
    private static final int MATCH_LEVEL = 1;
    private static final int INDEX = 2;
    private static final boolean ENABLED = true;

    @Override
    protected Mapper<Matcher, DbPlatformMatcher> getMapper(ModelMapper modelMapper) {
        return new MatcherToDbPlatformMatcherMapper(modelMapper);
    }

    @Test
    void testMap() {
        final Matcher matcher = new Matcher(MATCHER_NAME, HIERARCHY_NAME, MATCH_LEVEL, ENABLED, INDEX);
        final DbPlatformMatcher result = mapper.map(matcher);
        assertEquals(matcher.getMatchLevel(), result.getMatchLevel());
        assertEquals(matcher.getIndex(), result.getIndex());
        assertEquals(matcher.isEnabled(), result.isEnabled());
    }

}
