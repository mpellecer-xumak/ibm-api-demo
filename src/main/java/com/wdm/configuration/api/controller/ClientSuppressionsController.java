package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Suppression;
import com.wdm.configuration.api.response.ClientSuppressionResponse;
import com.wdm.configuration.api.service.ClientSuppressionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/config/clients")
@Tag(name = "Client Suppressions Configuration", description = "The Client Suppressions Configuration API documentation")
public class ClientSuppressionsController {
    private final ClientSuppressionService clientSuppressionService;

    @Operation(summary = "Associate suppressions to a client", description = "Associates an existing suppression to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Suppression was associated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {"/{clientId}/suppressions"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Suppression> associateSuppression(
            final @Valid @RequestBody Suppression suppression,
            @PathVariable final String clientId) {
        return this.associateSuppression(suppression, clientId, null);
    }

    @Operation(summary = "Associate suppressions to a client", description = "Associates an existing suppression to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Suppression was associated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {"/{clientId}/{subClientId}/suppressions"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Suppression> associateSuppression(
            final @Valid @RequestBody Suppression suppression,
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final Suppression result = clientSuppressionService.associateSuppression(clientId, subClientId, suppression);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Get suppression associated with a client", description = "Gets Suppression for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved configuration data"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}/suppressions"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientSuppressionResponse> getSuppressionIdentityResolutionConfiguration(
            @PathVariable final String clientId) {
        return this.getSuppressionIdentityResolutionConfiguration(clientId, null);
    }

    @Operation(summary = "Get suppression associated with a client", description = "Gets Suppression for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved configuration data"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}/{subClientId}/suppressions"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientSuppressionResponse> getSuppressionIdentityResolutionConfiguration(
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final List<Suppression> suppressions = clientSuppressionService.getSuppressionClientConfig(clientId, subClientId);
        final ClientSuppressionResponse response = ClientSuppressionResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .suppressions(suppressions)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update a suppression associated with a client", description = "Update Suppression for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The Suppression was updated correctly"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {"/{clientId}/suppressions"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientSuppressionResponse> updateClientSuppresion(
            final @Valid @RequestBody Suppression suppression,
            @PathVariable final String clientId) {
        return this.updateClientSuppresion(suppression, clientId, null);
    }

    @Operation(summary = "Update a suppression associated with a client", description = "Update Suppression for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The Suppression was updated correctly"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {"/{clientId}/{subClientId}/suppressions"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientSuppressionResponse> updateClientSuppresion(
            final @Valid @RequestBody Suppression suppression,
            @PathVariable final String clientId,
            @PathVariable final String subClientId) {
        final List<Suppression> suppressions = clientSuppressionService.updateClientSuppresion(suppression, clientId, subClientId);
        final ClientSuppressionResponse response = ClientSuppressionResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .suppressions(suppressions)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a suppression associated with a client", description = "Delete Suppression for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The Suppression was deleted correctly"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{clientId}/suppressions/{suppressionName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientSuppressionResponse> deleteClientSuppression(
            @PathVariable final String clientId,
            @PathVariable final String suppressionName) {
        return this.deleteClientSuppression(clientId, null, suppressionName);
    }

    @Operation(summary = "Delete a suppression associated with a client", description = "Delete Suppression for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The Suppression was deleted correctly"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{clientId}/{subClientId}/suppressions/{suppressionName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientSuppressionResponse> deleteClientSuppression(
            @PathVariable final String clientId,
            @PathVariable final String subClientId,
            @PathVariable final String suppressionName) {
        final List<Suppression> suppressions = clientSuppressionService.deleteClientSuppression(suppressionName, clientId, subClientId);
        final ClientSuppressionResponse response = ClientSuppressionResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .suppressions(suppressions)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
