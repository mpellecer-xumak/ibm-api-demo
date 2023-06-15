package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MatcherToDbMatcherMapperTest extends BaseMapperTest<SimpleName, DbMatcher> {
    private static final String MATCHER_NAME = "my_matcher";

    @Override
    protected Mapper<SimpleName, DbMatcher> getMapper(final ModelMapper modelMapper) {
        return new MatcherToDbMatcherMapper(modelMapper);
    }

    @Test
    void testMap() {
        final var matcher = new SimpleName(MATCHER_NAME);
        final var dbMatcher = mapper.map(matcher);
        assertEquals(MATCHER_NAME, dbMatcher.getName());
        assertNull(dbMatcher.getClientMatchers());
        assertNull(dbMatcher.getPlatformMatchers());
    }

}
