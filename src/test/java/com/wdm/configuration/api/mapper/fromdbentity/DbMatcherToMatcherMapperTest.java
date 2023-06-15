package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbMatcherToMatcherMapperTest extends BaseMapperTest<DbMatcher, SimpleName> {
    private static final String MATCHER_NAME = "my_matcher";

    @Override
    protected Mapper<DbMatcher, SimpleName> getMapper(final ModelMapper modelMapper) {
        return new DbMatcherToMatcherMapper(modelMapper);
    }

    @Test
    void testMap() {
        final var dbMatcher = new DbMatcher();
        dbMatcher.setName(MATCHER_NAME);
        final var matcher = mapper.map(dbMatcher);
        assertEquals(MATCHER_NAME, matcher.getName());
    }
}
