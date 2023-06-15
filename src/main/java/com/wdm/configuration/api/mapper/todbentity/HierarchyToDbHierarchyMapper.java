package com.wdm.configuration.api.mapper.todbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Hierarchy;
import com.wdm.configuration.api.persistence.entity.DbHierarchy;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
@Component
public class HierarchyToDbHierarchyMapper extends Mapper<Hierarchy, DbHierarchy> {

    protected HierarchyToDbHierarchyMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<DbHierarchy> getTargetType() {
        return DbHierarchy.class;
    }
}
