package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.service.SuppressionsService;
import com.wdm.configuration.api.request.SuppressionsRequest;
import com.wdm.configuration.api.response.GenericListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@RequestMapping("/config/suppressions")
@Tag(name = "Suppressions", description = "Endpoints related to suppressions configuration")
public class SuppressionsController {

    private final SuppressionsService suppressionsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericListResponse<Suppression>> getSuppressions() {
        final var suppressions = suppressionsService.getAllSuppressions();
        final var result = GenericListResponse.<Suppression>builder().response(suppressions).build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Create suppressions",
            description = "Creates a list of suppressions into the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The suppressions were created"),
                    @ApiResponse(responseCode = "400", description = "The request is not valid"),
                    @ApiResponse(responseCode = "500", description = "Problem handling the request.")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericListResponse<Suppression>> createSuppressions(
            final @RequestBody @Valid SuppressionsRequest suppressionsRequest) {
        final var createdSuppressions = suppressionsService.createSuppressions(suppressionsRequest.getSuppressions());
        final var result = GenericListResponse.<Suppression>builder().response(createdSuppressions).build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Deletes a suppression.",
            description = "Deletes a suppression.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully deleted suppression."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Suppression not found."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = "/{suppressionName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeSuppression(@PathVariable final String suppressionName) {
        suppressionsService.deleteSuppression(suppressionName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(summary = "Update suppression",
            description = "Updates a suppression information",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The suppressions was updated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = "The request is not valid"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Suppression not found"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Suppression> updateSuppressions(
            final @RequestBody @Valid Suppression suppression) {
        final Suppression updatedSuppression = suppressionsService.updateSuppression(suppression);
        return new ResponseEntity<>(updatedSuppression, HttpStatus.OK);
    }
}
