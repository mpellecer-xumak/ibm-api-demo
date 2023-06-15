package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.persistence.entity.DbAppend;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppendToDbAppendMapperTest extends BaseMapperTest<Append, DbAppend> {
    private static final String APPEND_NAME = "myAppend";
    private static final Boolean ENABLE_FUZZY_GENDER = true;
    private static final Integer ADDRESS_THRESHOLD = 1;

    @Override
    protected Mapper<Append, DbAppend> getMapper(ModelMapper modelMapper) {
        return new AppendToDbAppendMapper(modelMapper);
    }

    @Test
    void testMap() {
        final Append append = new Append();
        append.setName(APPEND_NAME);
        append.setEnableFuzzyGenderFilter(ENABLE_FUZZY_GENDER);
        append.setAddressThreshold(ADDRESS_THRESHOLD);

        final DbAppend dbAppend = mapper.map(append);

        assertEquals(APPEND_NAME, dbAppend.getName());
        assertEquals(ENABLE_FUZZY_GENDER, dbAppend.getEnableFuzzyGenderFilter());
        assertEquals(ADDRESS_THRESHOLD, dbAppend.getAddressThreshold());
    }

}
