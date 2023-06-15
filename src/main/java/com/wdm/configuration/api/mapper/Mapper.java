package com.wdm.configuration.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("PMD")
public abstract class Mapper<S, T> {
    protected final ModelMapper modelMapper;

    protected Mapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configure();
    }

    protected void configure() {
    }

    protected abstract Class<T> getTargetType();

    public T map(final S source) {
        if(Objects.isNull(source)) {
            return null;
        }
        return modelMapper.map(source, getTargetType());
    }

    public List<T> map(final Collection<S> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            Collections.emptyList();
        }
        return sourceList.stream().map(this::map).collect(Collectors.toList());
    }
}
