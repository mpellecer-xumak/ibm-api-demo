package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.BaseMapperTest;
import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbAggregate;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AggregateToDbAggregateMapperTest extends BaseMapperTest<Aggregate<SimpleName>, DbAggregate> {
    @Override
    protected Mapper<Aggregate<SimpleName>, DbAggregate> getMapper(ModelMapper modelMapper) {
        return new AggregateToDbAggregate(modelMapper);
    }
    @Test
    void testMap(){
        final Aggregate<SimpleName> aggregate = new Aggregate<SimpleName>("name",false, Collections.emptyList());
        final var dbAggregate = mapper.map(aggregate);
        assertEquals(aggregate.getName(), dbAggregate.getName());
        assertNull(dbAggregate.getBundles());
        assertEquals(aggregate.isEnabled(), dbAggregate.isEnabled());
    }
}
