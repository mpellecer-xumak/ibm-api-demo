package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.wdm.configuration.api.persistence.entity.DbAppend;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;

class DbAppendToAppendTest extends BaseMapperTest<DbAppend, Append> {

    @Override
    protected Mapper<DbAppend, Append> getMapper(ModelMapper modelMapper) {
        return new DbAppendToAppendMapper(modelMapper);
    }

    @Test
    void testMapSingleItem() {
        final DbAppend append = getDbAppend();
        final Append result = mapper.map(append);

        assertEquals(append.getName(), result.getName());
        assertEquals(append.getAddressThreshold(), result.getAddressThreshold());
        assertEquals(append.getDateOfBirthRangeYears(), result.getDateOfBirthRangeYears());
        assertEquals(append.getHouseholdThreshold(), result.getHouseholdThreshold());
        assertEquals(append.getLastNameThreshold(), result.getLastNameThreshold());
        assertEquals(append.getResidentThreshold(), result.getResidentThreshold());
        assertEquals(append.getIndividualThreshold(), result.getIndividualThreshold());
        assertEquals(append.getEnabled(), result.isEnabled());
        assertEquals(append.getEnableFuzzyGenderFilter(), result.getEnableFuzzyGenderFilter());
        assertEquals(append.getSuppressFederalDoNotCall(), result.getSuppressFederalDoNotCall());
        assertEquals(append.getSuppressFederalDoNotCall(), result.getSuppressFederalDoNotCall());
        assertEquals(append.getSuppressStateDoNotCall(), result.getSuppressStateDoNotCall()); 
    }

    @Test
    void testMapListItem() {
        final DbAppend append = getDbAppend();
        final List<Append> appends = mapper.map(List.of(append));
        final Append result = appends.get(0);

        assertEquals(append.getName(), result.getName());
        assertEquals(append.getAddressThreshold(), result.getAddressThreshold());
        assertEquals(append.getDateOfBirthRangeYears(), result.getDateOfBirthRangeYears());
        assertEquals(append.getHouseholdThreshold(), result.getHouseholdThreshold());
        assertEquals(append.getLastNameThreshold(), result.getLastNameThreshold());
        assertEquals(append.getResidentThreshold(), result.getResidentThreshold());
        assertEquals(append.getIndividualThreshold(), result.getIndividualThreshold());
        assertEquals(append.getEnabled(), result.isEnabled());
        assertEquals(append.getEnableFuzzyGenderFilter(), result.getEnableFuzzyGenderFilter());
        assertEquals(append.getSuppressFederalDoNotCall(), result.getSuppressFederalDoNotCall());
        assertEquals(append.getSuppressFederalDoNotCall(), result.getSuppressFederalDoNotCall());
        assertEquals(append.getSuppressStateDoNotCall(), result.getSuppressStateDoNotCall()); 
    }

    private DbAppend getDbAppend() {
        final DbAppend append = new DbAppend();
        append.setName("clientId");
        append.setAddressThreshold(1);
        append.setDateOfBirthRangeYears(99);
        append.setHouseholdThreshold(2);
        append.setLastNameThreshold(3);
        append.setResidentThreshold(4);
        append.setIndividualThreshold(5);
        append.setEnabled(false);
        append.setEnableFuzzyGenderFilter(true);
        append.setSuppressFederalDoNotCall(false);
        append.setSuppressFederalDoNotCall(false);
        append.setSuppressStateDoNotCall(true);
        return append;
    }

}
