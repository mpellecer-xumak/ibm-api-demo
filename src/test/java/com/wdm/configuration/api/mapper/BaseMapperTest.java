package com.wdm.configuration.api.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;

public abstract class BaseMapperTest<S, T> {
    protected Mapper<S, T> mapper;

    protected abstract Mapper<S, T> getMapper(final ModelMapper modelMapper);

    @BeforeEach
    protected void setup() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        mapper = getMapper(modelMapper);
    }
}
