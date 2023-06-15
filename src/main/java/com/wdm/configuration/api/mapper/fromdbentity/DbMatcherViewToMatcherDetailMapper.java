package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.persistence.view.ClientMatcherView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DbMatcherViewToMatcherDetailMapper extends Mapper<ClientMatcherView, Matcher> {
    protected DbMatcherViewToMatcherDetailMapper(final ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Class<Matcher> getTargetType() {
        return Matcher.class;
    }

    @Override
    public void configure() {
        modelMapper.typeMap(ClientMatcherView.class, Matcher.class)
                .addMapping(ClientMatcherView::getMatcherName, Matcher::setName);
    }

    @Override
    public List<Matcher> map(final Collection<ClientMatcherView> views) {
        return super.map(views).stream()
                .sorted(Comparator.comparingInt(Matcher::getIndex))
                .collect(Collectors.toList());
    }
}
