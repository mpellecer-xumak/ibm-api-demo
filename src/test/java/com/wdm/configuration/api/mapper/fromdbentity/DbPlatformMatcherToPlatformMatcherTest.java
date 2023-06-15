package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.PlatformMatcher;
import com.wdm.configuration.api.persistence.entity.DbHierarchy;
import com.wdm.configuration.api.persistence.entity.DbMatcher;
import com.wdm.configuration.api.persistence.entity.DbPlatformMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class DbPlatformMatcherToPlatformMatcherTest extends BaseMapperTest<DbPlatformMatcher, PlatformMatcher> {

    private static final String HIERARCHY_1 = "INSIGHT";
    private static final String HIERARCHY_2 = "IDENTITY";
    private static final String HIERARCHY_3 = "INSIGHT_HOUSEHOLD";
    private static final String MATCHER_1 = "NAME_ADDRESS";
    private static final String MATCHER_2 = "EMAIL_PLAINTEXT";

    private List<PlatformMatcher> platformMatchers;

    @Override
    protected Mapper<DbPlatformMatcher, PlatformMatcher> getMapper(final ModelMapper modelMapper) {
        return new DbPlatformMatcherToPlatformMatcher(modelMapper);
    }

    @BeforeEach
    void initialize() {
        final DbPlatformMatcher dbPlatformMatcher1 = getDbPlatformMatcher(HIERARCHY_1, MATCHER_1);
        final DbPlatformMatcher dbPlatformMatcher2 = getDbPlatformMatcher(HIERARCHY_2, MATCHER_2);
        final DbPlatformMatcher dbPlatformMatcher3 = getDbPlatformMatcher(HIERARCHY_3, MATCHER_2);
        final List<DbPlatformMatcher> dbPlatformMatchers = List.of(dbPlatformMatcher1, dbPlatformMatcher2, dbPlatformMatcher3);
        platformMatchers = mapper.map(dbPlatformMatchers);
    }

    @Test
    void testExpectedPlatformMatchersAmount() {
        assertEquals(3, platformMatchers.size());
    }

    @Test
    void testPlatformMatchersHaveItsRespectiveMatchersAmount() {
        final PlatformMatcher platformMatcher1 = platformMatchers.get(0);
        final PlatformMatcher platformMatcher2 = platformMatchers.get(1);
        final PlatformMatcher platformMatcher3 = platformMatchers.get(2);
        assertEquals(1, platformMatcher1.getMatcherDetails().size());
        assertEquals(1, platformMatcher2.getMatcherDetails().size());
        assertEquals(1, platformMatcher3.getMatcherDetails().size());
    }

    private DbPlatformMatcher getDbPlatformMatcher(final String hierarchyName, final String matcherName) {
        final DbPlatformMatcher dbPlatformMatcher = new DbPlatformMatcher();
        dbPlatformMatcher.setMatchLevel(1);
        dbPlatformMatcher.setIndex(1);
        dbPlatformMatcher.setId(1);
        dbPlatformMatcher.setEnabled(Boolean.TRUE);

        DbHierarchy dbHierarchy = new DbHierarchy();
        dbHierarchy.setName(hierarchyName);

        dbPlatformMatcher.setHierarchy(dbHierarchy);
        DbMatcher dbMatcher = new DbMatcher();

        dbMatcher.setName(matcherName);
        dbPlatformMatcher.setMatcher(dbMatcher);

        return dbPlatformMatcher;
    }
}


