package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.persistence.view.ClientAggregateView;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class DbAggregateBundleToBundleMapper extends Mapper<ClientAggregateView, Bundle> {
    protected DbAggregateBundleToBundleMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<Bundle> getTargetType() {
        return Bundle.class;
    }

    @Override
    public List<Bundle> map(final Collection<ClientAggregateView> aggregateViews) {
        return aggregateViews.stream()
                .filter(cav -> StringUtils.isNotBlank(cav.getBundleName()))
                .collect(Collectors.groupingBy(
                        ClientAggregateView::getBundleName,
                        Collectors.collectingAndThen(toList(), this::mapAggregateViewsToAttributes)))
                .entrySet()
                .stream()
                .map(entry -> new Bundle(entry.getKey(), entry.getValue()))
                .collect(toList());
    }

    private List<Attribute> mapAggregateViewsToAttributes(final List<ClientAggregateView> aggregateViews) {
        return aggregateViews.stream()
                .map(ClientAggregateView::getAttributeName)
                .map(Attribute::new)
                .collect(toList());
    }
}
