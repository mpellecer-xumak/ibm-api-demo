package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.persistence.entity.DbAggregate;
import com.wdm.configuration.api.persistence.entity.DbBundle;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DbAggregatesToAggregateMapperTest extends BaseMapperTest<DbAggregate, Aggregate<Bundle>> {
    private static final String AGGREGATE_1 = "aggregate1";
    private static final String AGGREGATE_2 = "aggregate2";
    private static final String AGGREGATE_3 = "aggregate3";
    private static final String BUNDLE_NAME = "MyAwesomeBundle";

    @Override
    protected Mapper<DbAggregate, Aggregate<Bundle>> getMapper(final ModelMapper modelMapper) {
        final DbBundleToBundleMapper bundleMapper = new DbBundleToBundleMapper(modelMapper, null);
        return new DbAggregatesToAggregateMapper(modelMapper, bundleMapper);
    }

    @Test
    void testListMap() {
        final DbAggregate view1 = getClientAggregateView(AGGREGATE_1);
        final DbAggregate view2 = getClientAggregateView(AGGREGATE_2);
        final DbAggregate view3 = getClientAggregateView(AGGREGATE_3);
        final List<DbAggregate> aggregateViews = List.of(view1, view2, view3);
        final List<Aggregate<Bundle>> aggregates = mapper.map(aggregateViews);

        assertEquals(3, aggregates.size());

        for (final Aggregate<Bundle> aggregate : aggregates) {
            assertEquals(BUNDLE_NAME, aggregate.getBundles().get(0).getName());
        }
    }

    private DbAggregate getClientAggregateView(final String aggregateName) {
        final DbAggregate dbAggregate = new DbAggregate();
        dbAggregate.setName(aggregateName);
        dbAggregate.setEnabled(Boolean.TRUE);
        DbBundle dbBundle = new DbBundle();
        dbBundle.setName(BUNDLE_NAME);
        dbAggregate.setBundles(List.of(dbBundle));
        return dbAggregate;
    }
}