package com.wdm.configuration.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when
;
import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.mapper.fromdbentity.DbAttributeToAttributeMapper;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.persistence.entity.DbAttribute;
import com.wdm.configuration.api.persistence.entity.DbBundle;
import com.wdm.configuration.api.persistence.repository.AttributeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {
    private static final String ATTR_NAME = "my-attribute";
    public static final String NOT_FOUND = "Not found";
    public static final String CONFLICT = "belongs to at least one bundle.";

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private AttributeService service;

    @Captor
    private ArgumentCaptor<List<DbAttribute>> newAttributesCaptor;

    @Mock
    private DbAttributeToAttributeMapper attributesMapper;

    @Test
    void testCreateAttributes() {
        final var dbAttribute = new DbAttribute(ATTR_NAME);
        when(attributeRepository.findByNameIn(anyList())).thenReturn(Collections.emptyList());
        when(attributeRepository.saveAll(anyList())).thenReturn(List.of(dbAttribute));
        final var attributes = List.of(getAttribute());

        final var result = service.createAttributes(attributes);
        
        verify(attributeRepository, times(1)).saveAll(newAttributesCaptor.capture());
        final var instanceToSave = newAttributesCaptor.getValue().get(0);
        assertEquals(ATTR_NAME, instanceToSave.getName());
        assertEquals(1, result.size());
        assertEquals(ATTR_NAME, result.get(0).getName());
    }

    @Test
    void testCreateAttributes_NoNewAttributes() {
        final var dbAttribute = new DbAttribute(ATTR_NAME);
        when(attributeRepository.findByNameIn(anyList())).thenReturn(List.of(dbAttribute));
        when(attributeRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        final var attributes = List.of(getAttribute());

        final var result = service.createAttributes(attributes);
        
        verify(attributeRepository, times(1)).saveAll(newAttributesCaptor.capture());
        final var newItems = newAttributesCaptor.getValue();
        assertEquals(0, newItems.size());
        assertEquals(1, result.size());
        assertEquals(ATTR_NAME, result.get(0).getName());
    }

    @Test
    void testCreateAttributes_EmptyParam() {
        final var result = service.createAttributes(Collections.emptyList());
        assertEquals(0, result.size());
        verifyNoInteractions(attributeRepository);
    }

    @Test
    void testCreateAttributes_NullParam() {
        final var result = service.createAttributes(null);
        assertEquals(0, result.size());
        verifyNoInteractions(attributeRepository);
    }

    @Test
    void testDeleteAttribute() {
        final var dbAttribute = new DbAttribute(ATTR_NAME);
        final var attribute = new Attribute(ATTR_NAME);

        when(attributeRepository.findByName(ATTR_NAME))
                .thenReturn(Optional.of(dbAttribute));
        when(mapper.map(dbAttribute, Attribute.class))
                .thenReturn(attribute);

        final var response = service.deleteAttribute(ATTR_NAME);

        assertEquals(ATTR_NAME, response.getName());
    }

    @Test
    void testDeleteAttribute_NotFoundError() {
        when(attributeRepository.findByName(ATTR_NAME))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(HttpNotFoundException.class,
                () -> service.deleteAttribute(ATTR_NAME));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(NOT_FOUND));
    }

    @Test
    void testDeleteAttribute_ConflictError() {
        final var dbAttribute = new DbAttribute(ATTR_NAME);
        dbAttribute.setBundles(List.of(new DbBundle()));

        when(attributeRepository.findByName(ATTR_NAME))
                .thenReturn(Optional.of(dbAttribute));

        final var exception = assertThrows(HttpConflictException.class,
                () -> service.deleteAttribute(ATTR_NAME));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(CONFLICT));
    }

    @Test
    void testDeleteAttribute_NullAttribute_NotFoundExpected() {
        final var exception = assertThrows(HttpNotFoundException.class,
                () -> service.deleteAttribute(null));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(NOT_FOUND));
    }

    @Test
    void testGetAttributes() {
        DbAttribute dbAttribute = new DbAttribute();
        dbAttribute.setName(ATTR_NAME);

        when(attributeRepository.findAll()).thenReturn(List.of(dbAttribute));
        when(attributesMapper.map(anyList())).thenReturn(List.of(getAttribute()));

        List<Attribute> attributes =  service.getAttributes();
        assertNotNull(attributes);
    }

    @Test
    void testGetAttributes_WhenNoAttributesFound() {
        when(attributeRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(HttpNotFoundException.class, () -> service.getAttributes());
    }

    private Attribute getAttribute() {
        return new Attribute(ATTR_NAME);
    }
}
