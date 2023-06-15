package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.persistence.view.ClientAppendView;

class DbClientAppendViewToAppendTest extends BaseMapperTest<ClientAppendView, Append> {

    @Override
    protected Mapper<ClientAppendView, Append> getMapper(ModelMapper modelMapper) {
        return new DbClientAppendViewToAppendMapper(modelMapper);
    }

    @Test
    void testMapSingleItem() {
        final ClientAppendView append = getClientAppendView();
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
        final ClientAppendView append = getClientAppendView();
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

    private ClientAppendView getClientAppendView() {
        final ClientAppendView appendView = new ClientAppendView();
        appendView.setName("clientId");
        appendView.setAddressThreshold(1);
        appendView.setDateOfBirthRangeYears(99);
        appendView.setHouseholdThreshold(2);
        appendView.setLastNameThreshold(3);
        appendView.setResidentThreshold(4);
        appendView.setIndividualThreshold(5);
        appendView.setEnabled(false);
        appendView.setEnableFuzzyGenderFilter(true);
        appendView.setSuppressFederalDoNotCall(false);
        appendView.setSuppressFederalDoNotCall(false);
        appendView.setSuppressStateDoNotCall(true);
        return appendView;
    }

}
