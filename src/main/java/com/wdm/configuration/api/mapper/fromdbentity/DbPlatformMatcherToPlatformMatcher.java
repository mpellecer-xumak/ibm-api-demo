package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.model.PlatformMatcher;
import com.wdm.configuration.api.persistence.entity.DbPlatformMatcher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class DbPlatformMatcherToPlatformMatcher extends Mapper<DbPlatformMatcher, PlatformMatcher> {

    protected DbPlatformMatcherToPlatformMatcher(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<PlatformMatcher> getTargetType() {
        return PlatformMatcher.class;
    }

    @Override
    public List<PlatformMatcher> map(final Collection<DbPlatformMatcher> dbPlatformMatchers) {
        return dbPlatformMatchers.stream()
                .collect(Collectors.groupingBy(
                        DbPlatformMatcher::getHierarchy,
                        Collectors.collectingAndThen(toList(), this::mapDbMatcherToMatcher)))
                .entrySet()
                .stream()
                .map(entry -> new PlatformMatcher(entry.getKey().getName(), entry.getValue()))
                .collect(toList());
    }

    private List<Matcher> mapDbMatcherToMatcher(final Collection<DbPlatformMatcher> dbPlatformMatchers) {
        return dbPlatformMatchers.stream()
                .map(entry -> new Matcher(entry.getMatcher().getName(), entry.getMatchLevel(), entry.isEnabled(), entry.getIndex()))
                .collect(toList());
    }
}