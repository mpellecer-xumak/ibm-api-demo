package com.wdm.configuration.api.service;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.mapper.todbentity.HierarchyToDbHierarchyMapper;
import com.wdm.configuration.api.model.Hierarchy;
import com.wdm.configuration.api.persistence.entity.DbHierarchy;
import com.wdm.configuration.api.persistence.repository.HierarchyRespository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.wdm.configuration.api.exception.HttpNotFoundException;
@ExtendWith(MockitoExtension.class)
public class HierarchyServiceTest {
    private static final String HIERARCHY_NAME = "myHierarchy";
    private final Hierarchy HIERARCHY = new Hierarchy("unitTest2");
    private final DbHierarchy DBHIERARCHY = new DbHierarchy();
    @Mock
    private HierarchyRespository hierarchyRespository;
    @Mock
    private HierarchyToDbHierarchyMapper hierarchyToDbHierarchyMapper;
    @InjectMocks
    private HierarchyService service;
    @Test
    void testCreateHierarchies(){
        final  var hierarchies = getList(HIERARCHY);
        when(hierarchyRespository.findAllByNameIn(anyList())).thenReturn(Collections.emptyList());
        when(hierarchyToDbHierarchyMapper.map(hierarchies)).thenReturn(List.of(DBHIERARCHY));


        final var result = service.createHierarchies(hierarchies);
        assertNotNull(result);
    }
    @Test
    void testCreateHierarchiesExist(){
        final  var hierarchies = getList(HIERARCHY);
        when(hierarchyRespository.findAllByNameIn(anyList())).thenReturn(List.of(DBHIERARCHY));
        assertThrows(HttpConflictException.class, ()->service.createHierarchies(hierarchies));
    }
    @Test
    void testGetDbHierarchy() {
        when(hierarchyRespository.findByName(HIERARCHY_NAME)).thenReturn(new DbHierarchy());
        final DbHierarchy dbHierarchy = service.getDbHierarchy(HIERARCHY_NAME);
        assertNotNull(dbHierarchy);
    }

    @Test
    void testGetDbHierarchy_NotFound() {
        when(hierarchyRespository.findByName(HIERARCHY_NAME)).thenReturn(null);
        assertThrows(HttpNotFoundException.class, () -> service.getDbHierarchy(HIERARCHY_NAME));
    }
    private List<Hierarchy> getList(Hierarchy hierarchy){
        final List<Hierarchy> result = List.of(hierarchy);
        return  result;
    }
}
