package com.wdm.configuration.api.mapper.fromdbentity;

import com.wdm.configuration.api.mapper.Mapper;
import com.wdm.configuration.api.model.Deployment;
import com.wdm.configuration.api.model.VersionManagement;
import com.wdm.configuration.api.persistence.entity.DbDeploymentVersion;
import org.apache.commons.lang3.EnumUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DbDeploymentToVersionManagementMapper extends Mapper<DbDeploymentVersion, VersionManagement> {
    protected DbDeploymentToVersionManagementMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    protected Class<VersionManagement> getTargetType() {
        return VersionManagement.class;
    }

    @Override
    public void configure() {
        modelMapper.typeMap(DbDeploymentVersion.class, VersionManagement.class)
                .addMapping(DbDeploymentVersion::getName, VersionManagement::setEntity)
                .addMapping(DbDeploymentVersion::getColor, (vm, color) -> {
                    final var colorEnum = EnumUtils.getEnumIgnoreCase(Deployment.class, (String) color);
                    vm.setState(colorEnum);
                });
    }
}
