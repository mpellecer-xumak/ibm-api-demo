package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.HierarchyEnum;
import com.wdm.configuration.api.model.MatcherHierarchyGroup;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.persistence.view.ClientMatcherView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DbMatcherViewToMatcherMapper extends Mapper<ClientMatcherView, MatcherHierarchyGroup> {
    private final Mapper<ClientMatcherView, Matcher> detailsMapper;

    protected DbMatcherViewToMatcherMapper(final ModelMapper modelMapper, final Mapper<ClientMatcherView, Matcher> detailsMapper) {
        super(modelMapper);
        this.detailsMapper = detailsMapper;
    }

    @Override
    public Class<MatcherHierarchyGroup> getTargetType() {
        return MatcherHierarchyGroup.class;
    }

    @Override
    public List<MatcherHierarchyGroup> map(final Collection<ClientMatcherView> source) {
        return source.stream()
                .collect(Collectors.groupingBy(
                        el -> HierarchyEnum.valueOf(el.getHierarchyName()),
                        Collectors.collectingAndThen(Collectors.toList(), detailsMapper::map)))
                .entrySet()
                .stream()
                .map(entry -> {
                    final MatcherHierarchyGroup matcher = new MatcherHierarchyGroup();
                    matcher.setHierarchy(entry.getKey());
                    matcher.setMatcherDetails(entry.getValue());
                    return matcher;
                })
                .collect(Collectors.toList());
    }

}
