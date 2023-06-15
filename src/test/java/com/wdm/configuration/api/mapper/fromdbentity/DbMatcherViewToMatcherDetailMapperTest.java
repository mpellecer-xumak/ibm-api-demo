package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.persistence.view.ClientMatcherView;

@ExtendWith(MockitoExtension.class)
class DbMatcherViewToMatcherDetailMapperTest extends BaseMapperTest<ClientMatcherView, Matcher> {
    @Override
    protected Mapper<ClientMatcherView, Matcher> getMapper(ModelMapper modelMapper) {
        return new DbMatcherViewToMatcherDetailMapper(modelMapper);
    }

    @Test
    void mapSingleItem() {
        final ClientMatcherView matcherView = getMatcherView(1);

        final Matcher matcherDetails = mapper.map(matcherView);

        assertEquals(matcherView.getMatcherName(), matcherDetails.getName());
        assertEquals(matcherView.getMatchLevel(), matcherDetails.getMatchLevel());
        assertEquals(matcherView.getIndex(), matcherDetails.getIndex());
    }

    @Test
    void mapList() {
        final ClientMatcherView matcherView = getMatcherView(10);
        final ClientMatcherView matcherView2 = getMatcherView(2);
        final List<Matcher> matcherDetails = mapper.map(List.of(matcherView, matcherView2));

        final Matcher firstMappedValue = matcherDetails.get(0);
        final Matcher secondMappedValue = matcherDetails.get(1);

        assertNotEquals(firstMappedValue, secondMappedValue);
        assertNotEquals(firstMappedValue.getIndex(), secondMappedValue.getIndex());

        assertEquals(matcherView2.getIndex(), firstMappedValue.getIndex());
        assertEquals(matcherView.getIndex(), secondMappedValue.getIndex());

        assertEquals(matcherView2.getMatcherName(), firstMappedValue.getName());
        assertEquals(matcherView.getMatcherName(), secondMappedValue.getName());
    }

    private ClientMatcherView getMatcherView(final int index) {
        final ClientMatcherView view = new ClientMatcherView();
        view.setEnabled(Boolean.TRUE);
        view.setMatcherName("TheMatcher" + index);
        view.setHierarchyName("TheHierarchy");
        view.setMatchLevel(1);
        view.setIndex(index);
        return view;
    }
}