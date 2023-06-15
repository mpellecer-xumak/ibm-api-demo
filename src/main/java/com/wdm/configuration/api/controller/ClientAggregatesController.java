package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Aggregate;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.response.ClientAggregatesResponse;
import com.wdm.configuration.api.service.ClientAggregateService;
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
@RequestMapping("/config/clients")
@Tag(name = "Client Aggregates Configuration", description = "The Client Aggregates Configuration API documentation")
public class ClientAggregatesController {

    private final ClientAggregateService clientAggregateService;

    @Operation(summary = "Associates aggregates to a client",
            description = "Associate existing an aggregate to a client, you can specify the bundles that the aggregate will contain",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Client aggregate was associated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {"/{clientId}/aggregates"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAggregatesResponse> associateAggregate(
            final @Valid @RequestBody Aggregate<SimpleName> aggregate,
            @PathVariable final String clientId) {
        return upsertAggregate(aggregate, clientId, null);
    }

    @Operation(summary = "Associates aggregates to a client",
            description = "Associate existing an aggregate to a client, you can specify the bundles that the aggregate will contain",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Client aggregate was associated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {"/{clientId}/{subClientId}/aggregates"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAggregatesResponse> associateAggregate(
            final @Valid @RequestBody Aggregate<SimpleName> aggregate,
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        return upsertAggregate(aggregate, clientId, subClientId);
    }

    @Operation(summary = "Updates an aggregate assigned to a client",
            description = "Replaces the configuration for an aggregate associated to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The aggregate was updated correctly"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {"/{clientId}/aggregates"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAggregatesResponse> updateAggregate(
            final @Valid @RequestBody Aggregate<SimpleName> aggregate,
            @PathVariable final String clientId) {
        return upsertAggregate(aggregate, clientId, null);
    }

    @Operation(summary = "Updates an aggregate assigned to a client",
            description = "Replaces the configuration for an aggregate associated to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The aggregate was updated correctly"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {"/{clientId}/{subClientId}/aggregates"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAggregatesResponse> updateAggregate(
            final @Valid @RequestBody Aggregate<SimpleName> aggregate,
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        return upsertAggregate(aggregate, clientId, subClientId);
    }

    @Operation(summary = "Get Aggregates associated with a client", description = "Gets Aggregates for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved configuration data"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}/aggregates"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAggregatesResponse> getClientAggregates(
            @PathVariable final String clientId) {
        return this.getClientAggregates(clientId, null);
    }

    @Operation(summary = "Get Aggregates associated with a client", description = "Gets Aggregates for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved configuration data"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}/{subClientId}/aggregates"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAggregatesResponse> getClientAggregates(
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final List<Aggregate<Bundle>> aggregates = clientAggregateService.getClientAggregates(clientId, subClientId);
        final ClientAggregatesResponse response = ClientAggregatesResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .aggregates(aggregates)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Remove Aggregates associated with a client", description = "Removes Aggregates for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully removed aggregate"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{clientId}/aggregates/{aggregateName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAggregatesResponse> deleteClientAggregates(
            @PathVariable final String clientId,
            @PathVariable final String aggregateName) {
        return deleteClientAggregates(aggregateName, clientId, null);
    }
    @Operation(summary = "Remove Aggregates associated with a client", description = "Removes Aggregates for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully removed aggregate"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{clientId}/{subClientId}/aggregates/{aggregateName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAggregatesResponse> deleteClientAggregates(
            @PathVariable final String aggregateName,
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final List<Aggregate<Bundle>> aggregates = clientAggregateService.removeAggregateFromClient(clientId, subClientId, aggregateName);
        final ClientAggregatesResponse response = ClientAggregatesResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .aggregates(aggregates)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<ClientAggregatesResponse> upsertAggregate(
            final Aggregate<SimpleName> aggregate,
            final String clientId,
            final String subClientId) {
        final List<Aggregate<Bundle>> clientAggregates = clientAggregateService.assignAggregateToClient(clientId, subClientId, aggregate);
        final ClientAggregatesResponse response = ClientAggregatesResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .aggregates(clientAggregates)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
