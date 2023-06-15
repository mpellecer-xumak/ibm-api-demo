package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.persistence.entity.DbAggregate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AggregateToDbAggregate extends Mapper<Aggregate<SimpleName>, DbAggregate> {

    protected AggregateToDbAggregate(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbAggregate> getTargetType() {
        return DbAggregate.class;
    }
    @Override
    protected void configure() {
        modelMapper.createTypeMap(Aggregate.class, DbAggregate.class)
                .addMappings(mapper -> mapper.skip(DbAggregate::setBundles));
    }

}
