package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.todbentity.HierarchyToDbHierarchyMapper;
import com.wdm.configuration.api.model.Hierarchy;
import com.wdm.configuration.api.persistence.entity.DbHierarchy;
import com.wdm.configuration.api.persistence.repository.HierarchyRespository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
@Service
@AllArgsConstructor
public class HierarchyService {
    private HierarchyRespository hierarchyRespository;
    private HierarchyToDbHierarchyMapper hierarchyToDbHierarchyMapper;

    public List<Hierarchy> createHierarchies(List<Hierarchy> hierarchyList) {
        final List<String> hierarchyNames = hierarchyList.stream()
                .filter(Objects::nonNull)
                .map(Hierarchy::getName)
                .collect(Collectors.toList());
        if (hierarchyRespository.findAllByNameIn(hierarchyNames).size() > 0){
            String message = "One or more hierarchies already exist";
            throw new HttpConflictException(message);
        }
        List<DbHierarchy> dbHierarchyList = hierarchyToDbHierarchyMapper.map(hierarchyList);
        hierarchyRespository.saveAll(dbHierarchyList);

        return hierarchyList;
    }

    public DbHierarchy getDbHierarchy(final String name) {
        final DbHierarchy dbHierarchy = hierarchyRespository.findByName(name);
        if (Objects.isNull(dbHierarchy)) {
            final String message = String.format("The hierarchy '%s' does not exists", name);
            log.warn(message);
            throw new HttpNotFoundException(message);
        }
        return dbHierarchy;
    }
}
