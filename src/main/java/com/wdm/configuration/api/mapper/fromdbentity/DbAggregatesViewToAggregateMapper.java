package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.persistence.view.ClientAggregateView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DbAggregatesViewToAggregateMapper extends Mapper<ClientAggregateView, Aggregate<Bundle>> {
    private final Mapper<ClientAggregateView, Bundle> bundleMapper;

    protected DbAggregatesViewToAggregateMapper(final ModelMapper modelMapper, final Mapper<ClientAggregateView, Bundle> bundleMapper) {
        super(modelMapper);
        this.bundleMapper = bundleMapper;
    }

    @Override
    protected Class<Aggregate<Bundle>> getTargetType() {
        return (Class<Aggregate<Bundle>>) (Class) Aggregate.class;
    }

    @Override
    public List<Aggregate<Bundle>> map(final Collection<ClientAggregateView> clientAggregates) {
        return clientAggregates.stream()
                .collect(Collectors.groupingBy(
                        ClientAggregateView::getAggregateName,
                        Collectors.collectingAndThen(Collectors.toList(), this::collectToAggregate)))
                .values().stream()
                .collect(Collectors.toList());
    }

    private Aggregate<Bundle> collectToAggregate(final List<ClientAggregateView> aggregateViews) {
        final ClientAggregateView clientAggregateView = aggregateViews.get(0);
        final Aggregate<Bundle> aggregate = new Aggregate<>(
                clientAggregateView.getAggregateName(),
                clientAggregateView.isEnabled(),
                clientAggregateView.getBatchFileProcessingBundle());
        aggregate.setBundles(bundleMapper.map(aggregateViews));
        return aggregate;
    }
}
