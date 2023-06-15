package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.wdm.configuration.api.model.HierarchyEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.MatcherHierarchyGroup;
import com.wdm.configuration.api.persistence.view.ClientMatcherView;

@ExtendWith(MockitoExtension.class)
class DbMatcherViewToMatcherMapperTest extends BaseMapperTest<ClientMatcherView, MatcherHierarchyGroup> {
    private List<MatcherHierarchyGroup> matchers;

    @Override
    protected Mapper<ClientMatcherView, MatcherHierarchyGroup> getMapper(ModelMapper modelMapper) {
        final DbMatcherViewToMatcherDetailMapper matcherDetailMapper = new DbMatcherViewToMatcherDetailMapper(modelMapper);
        return new DbMatcherViewToMatcherMapper(modelMapper, matcherDetailMapper);
    }

    @Override
    @BeforeEach
    protected void setup() {
        super.setup();
        final ClientMatcherView matcherView = getMatcherView(2);
        final ClientMatcherView matcherView2 = getMatcherView(4);
        final ClientMatcherView matcherView3 = getMatcherView(5);
        final List<ClientMatcherView> matcherViewList = List.of(matcherView, matcherView2, matcherView3);
        this.matchers = mapper.map(matcherViewList);
    }

    @Test
    void testListMatchersGrouping() {
        final long identityCount = matchers.stream().filter(m -> HierarchyEnum.IDENTITY.equals(m.getHierarchy())).count();
        final long contactPointCount = matchers.stream().filter(m -> HierarchyEnum.INSIGHT_CONTACT_POINT.equals(m.getHierarchy())).count();

        assertEquals(1, identityCount);
        assertEquals(1, contactPointCount);
    }

    @Test
    void testListMap_DetailsCount() {

        final long identityCount = matchers.stream()
                .filter(m -> HierarchyEnum.IDENTITY.equals(m.getHierarchy()))
                .map(MatcherHierarchyGroup::getMatcherDetails)
                .mapToInt(List::size)
                .sum();
        final long contactPointCount = matchers.stream()
                .filter(m -> HierarchyEnum.INSIGHT_CONTACT_POINT.equals(m.getHierarchy()))
                .map(MatcherHierarchyGroup::getMatcherDetails)
                .mapToInt(List::size)
                .sum();

        assertEquals(2, identityCount);
        assertEquals(1, contactPointCount);
    }

    private ClientMatcherView getMatcherView(final int index) {
        final ClientMatcherView view = new ClientMatcherView();
        view.setEnabled(Boolean.TRUE);
        view.setMatcherName("TheMatcher" + index);
        final String hierarchy = index % 2 == 0 ? HierarchyEnum.IDENTITY.name() : HierarchyEnum.INSIGHT_CONTACT_POINT.name();
        view.setHierarchyName(hierarchy);
        view.setMatchLevel(1);
        view.setIndex(index);
        return view;
    }

}