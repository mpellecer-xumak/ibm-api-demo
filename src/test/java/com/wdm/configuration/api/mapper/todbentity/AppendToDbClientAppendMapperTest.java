package com.wdm.configuration.api.mapper.todbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.persistence.entity.DbClientAppend;

public class AppendToDbClientAppendMapperTest extends BaseMapperTest<Append, DbClientAppend> {
    private static final Boolean ENABLE_FUZZY_GENDER = true;
    private static final Integer ADDRESS_THRESHOLD = 1;

    @Override
    protected Mapper<Append, DbClientAppend> getMapper(ModelMapper modelMapper) {
        return new AppendToDbClientAppendMapper(modelMapper);
    }

    @Test
    void testMap() {
        final Append append = new Append();
        append.setEnableFuzzyGenderFilter(ENABLE_FUZZY_GENDER);
        append.setAddressThreshold(ADDRESS_THRESHOLD);

        final DbClientAppend dbAppend = mapper.map(append);

        assertEquals(ENABLE_FUZZY_GENDER, dbAppend.getEnableFuzzyGenderFilter());
        assertEquals(ADDRESS_THRESHOLD, dbAppend.getAddressThreshold());
    }

}
