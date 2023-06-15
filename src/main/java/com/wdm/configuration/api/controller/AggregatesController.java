package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.response.AllAggregatesResponse;
import com.wdm.configuration.api.service.AggregateService;
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
@RequestMapping("/config/aggregates")
@Tag(name = "Aggregates API", description = "The Aggregates API documentation")
public class AggregatesController {
    private static final String SUCCESSFULLY_MESSAGE = "Successfully created the aggregate.";

    private final AggregateService aggregateService;

    @Operation(summary = "Creates a new aggregate", description = "Creates an aggregate with the specified bundles", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_201_STATUS, description = SUCCESSFULLY_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aggregate<SimpleName>> createAggregate(final @Valid @RequestBody Aggregate<SimpleName> aggregate) {
        final Aggregate<SimpleName> result = aggregateService.createAggregate(aggregate);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Updates a new aggregate", description = "Updates an aggregate with the specified bundles", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_201_STATUS, description = SUCCESSFULLY_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aggregate<SimpleName>> updateAggregate(final @Valid @RequestBody Aggregate<SimpleName> aggregate) {
        final Aggregate<SimpleName> result = aggregateService.updateAggregate(aggregate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Retrieves all aggregates in the platform level.",
            description = "Retrieves all aggregates.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved list of aggregates."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Aggregates not found."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllAggregatesResponse> getAllAggregates() {
        final List<Aggregate<Bundle>> aggregateList = aggregateService.getAllAggregates();
        final AllAggregatesResponse allAggregatesResponse = new AllAggregatesResponse();
        allAggregatesResponse.setAggregates(aggregateList);
        return new ResponseEntity<>(allAggregatesResponse, HttpStatus.OK);
    }

    @Operation(summary = "Deletes an aggregate.",
            description = "Deletes an aggregate.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully deleted aggregate."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Aggregate not found."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = "/{aggregateName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeAggregate(@PathVariable final String aggregateName) {
        aggregateService.deleteAggregate(aggregateName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
