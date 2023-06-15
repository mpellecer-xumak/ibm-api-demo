package com.wdm.configuration.api.mapper.fromdbentity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.persistence.view.ClientAggregateView;

class DbAggregatesViewToAggregateMapperTest extends BaseMapperTest<ClientAggregateView, Aggregate<Bundle>> {
    private static final String AGGREGATE_1 = "aggregate1";
    private static final String AGGREGATE_2 = "aggregate2";
    private static final String AGGREGATE_3 = "aggregate3";
    private static final String BUNDLE_NAME = "MyAwesomeBundle";

    @Override
    protected Mapper<ClientAggregateView, Aggregate<Bundle>> getMapper(final ModelMapper modelMapper) {
        final DbAggregateBundleToBundleMapper bundleMapper = new DbAggregateBundleToBundleMapper(modelMapper);
        return new DbAggregatesViewToAggregateMapper(modelMapper, bundleMapper);
    }

    @Test
    void testListMap() {
        final ClientAggregateView view1 = getClientAggregateView(AGGREGATE_1);
        final ClientAggregateView view1_1 = getClientAggregateView(AGGREGATE_1);
        final ClientAggregateView view1_2 = getClientAggregateView(AGGREGATE_1);
        final ClientAggregateView view2 = getClientAggregateView(AGGREGATE_2);
        final ClientAggregateView view3 = getClientAggregateView(AGGREGATE_3);
        final List<ClientAggregateView> aggregateViews = List.of(view1, view2, view3, view1_1, view1_2);
        final List<Aggregate<Bundle>> aggregates = mapper.map(aggregateViews);

        assertEquals(3, aggregates.size());

        for (final Aggregate<Bundle> aggregate : aggregates) {
            assertEquals(BUNDLE_NAME, aggregate.getBundles().get(0).getName());
        }
    }

    private ClientAggregateView getClientAggregateView(final String aggregateName) {
        final ClientAggregateView view = new ClientAggregateView();
        view.setAggregateName(aggregateName);
        view.setBundleName(BUNDLE_NAME);
        view.setAttributeName("Attribute");
        view.setEnabled(Boolean.TRUE);
        return view;
    }
}