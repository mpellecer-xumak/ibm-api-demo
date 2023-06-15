package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.response.AttributeResponse;
import com.wdm.configuration.api.response.AttributesResponse;
import com.wdm.configuration.api.service.AttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/config/attributes")
@Tag(name = "Attributes API", description = "The Attributes API documentation")
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis"})
public class AttributesController {

    private final AttributeService attributeService;

    @Operation(summary = "Remove an attribute.",
            description = "Remove an attribute by name.",
            responses = {
                    @ApiResponse(
                            responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully removed."),
                    @ApiResponse(
                            responseCode = HttpCodes.HTTP_404_STATUS, description = "Attribute not found."),
                    @ApiResponse(
                            responseCode = HttpCodes.HTTP_409_STATUS, description = "Attribute belongs to a bundle"),
                    @ApiResponse(
                            responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)
    })
    @DeleteMapping(value = {"/{attributeName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AttributeResponse> deleteAttribute(
            @Valid @PathVariable final String attributeName) {
        final Attribute attribute = attributeService.deleteAttribute(attributeName);

        return AttributeResponse.build((response) -> {
            response.setAttributeName(attribute.getName());
        }, HttpStatus.OK);
    }

    @Operation(summary = "Retrieves all the attributes.",
            description = "Retrieves all the existing attributes.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved list of attributes."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Attributes not found."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AttributesResponse> getAllAttributes() {
        final List<Attribute> attributes = attributeService.getAttributes();
        final AttributesResponse response = new AttributesResponse(attributes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
