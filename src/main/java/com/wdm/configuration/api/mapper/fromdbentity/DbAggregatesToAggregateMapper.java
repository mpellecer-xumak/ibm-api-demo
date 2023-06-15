package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.persistence.entity.DbAggregate;
import com.wdm.configuration.api.persistence.entity.DbBundle;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DbAggregatesToAggregateMapper extends Mapper<DbAggregate, Aggregate<Bundle>> {

    private final DbBundleToBundleMapper bundleMapper;

    protected DbAggregatesToAggregateMapper(final ModelMapper modelMapper, final DbBundleToBundleMapper bundleMapper) {
        super(modelMapper);
        this.bundleMapper = bundleMapper;
    }

    @Override
    protected Class<Aggregate<Bundle>> getTargetType() {
        return (Class<Aggregate<Bundle>>) (Class) Aggregate.class;
    }

    @Override
    public List<Aggregate<Bundle>> map(final Collection<DbAggregate> dbAggregates) {
        return dbAggregates.stream().map(this::dbAggregateToAggregate).collect(toList());
    }

    private Aggregate<Bundle> dbAggregateToAggregate(final DbAggregate aggregate) {
        return new Aggregate<>(aggregate.getName(), aggregate.isEnabled(), mapDbBundleToBundle(aggregate.getBundles()));
    }

    private List<Bundle> mapDbBundleToBundle(final List<DbBundle> dbBundles) {
        return dbBundles.stream()
                .map(bundleMapper::mapWithoutAttributes)
                .collect(toList());
    }
}
