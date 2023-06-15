package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.persistence.repository.AttributeRepository;
import com.wdm.configuration.api.service.AttributeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AttributesController.class)
class AttributesControllerTest {
    private static final String BASE_PATH = "/config/attributes";
    private final static String DELETE_PATH = "/config/attributes/{attributeName}";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttributeService attributeService;

    @MockBean
    private AttributeRepository attributeRepository;

    private static final String ATTR_NAME = "test-attr";
    private final Attribute ATTRIBUTE = new Attribute(ATTR_NAME);

    @Test
    void deleteAnAttributeAndExpectOk() throws Exception {
        when(attributeService.deleteAttribute(ATTR_NAME))
                .thenReturn(ATTRIBUTE);
        mockMvc.perform(delete(DELETE_PATH, ATTR_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attributeName").value(ATTR_NAME))
        ;
    }

    @Test
    void deleteAnAttributeAndExpect404() throws Exception {
        final String exceptionMessage = "Not found";
        when(attributeService.deleteAttribute(ATTR_NAME))
                .thenThrow(new HttpNotFoundException(exceptionMessage));
        mockMvc.perform(delete(DELETE_PATH, ATTR_NAME))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(containsString(exceptionMessage)))
        ;
    }

    @Test
    void deleteAnAttributeAndExpect409() throws Exception {
        final String exceptionMessage = "Conflict found";
        when(attributeService.deleteAttribute(ATTR_NAME))
                .thenThrow(new HttpConflictException(exceptionMessage));
        mockMvc.perform(delete(DELETE_PATH, ATTR_NAME))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value(containsString(exceptionMessage)))
                ;
    }

    @Test
    void testGetAllAttributes() throws Exception {
        when(attributeService.getAttributes()).thenReturn(List.of(getAttribute()));
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.attributes[0].name").value(ATTR_NAME));
    }

    @Test
    void testGetAllAttributes_WhenNoAttributesFound() throws Exception {
        when(attributeService.getAttributes()).thenThrow(new HttpNotFoundException(""));
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isNotFound());
    }

    private Attribute getAttribute() {
        return new Attribute(ATTR_NAME);
    }
}
