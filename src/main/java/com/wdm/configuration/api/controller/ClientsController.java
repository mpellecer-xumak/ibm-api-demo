package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.ClientConfig;
import com.wdm.configuration.api.request.ClientCreationRequest;
import com.wdm.configuration.api.request.ClientUpdateRequest;
import com.wdm.configuration.api.response.ConfigurationResponse;
import com.wdm.configuration.api.service.ClientConfigService;
import com.wdm.configuration.api.service.ClientService;
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
@Tag(name = "Client Configuration API", description = "The Beta Client Configuration API documentation")
public class ClientsController {
    private static final String CONFIGURATION_NOT_FOUND_MESSAGE = "Configuration not found.";
    private static final String SUCCESSFULLY_MESSAGE = "Successfully retrieved configuration data.";

    private final ClientService clientService;
    private final ClientConfigService clientConfigService;

    @Operation(summary = "Identity Resolution Configuration", description = "Gets Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = SUCCESSFULLY_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = CONFIGURATION_NOT_FOUND_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfigurationResponse> getIdentityResolutionConfiguration(
            @PathVariable final String clientId) {
        return getClientConfigResponse(clientId, null);
    }

    @Operation(summary = "Identity Resolution Configuration", description = "Gets Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = SUCCESSFULLY_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = CONFIGURATION_NOT_FOUND_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}/{subClientId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfigurationResponse> getIdentityResolutionConfiguration(
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        return getClientConfigResponse(clientId, subClientId);
    }

    @Operation(summary = "Creates a client", description = "Creates a client with identity resolution configuration", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Client was created"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientConfig> createClient(final @Valid @RequestBody ClientCreationRequest clientRequest) {
        clientService.createClient(clientRequest);
        final var result = clientConfigService.getClientConfig(
                clientRequest.getClientId(), clientRequest.getSubClientId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Updates a client", description = "Updates a client with the new identity resolution configuration", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Client was updated"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {"/{clientId}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientConfig> updateClient(
            final @PathVariable String clientId,
            final @Valid @RequestBody ClientUpdateRequest clientRequest) {
        return updateClient(clientId, null, clientRequest);
    }

    @Operation(summary = "Updates a client", description = "Updates a client with the new identity resolution configuration", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Client was updated"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {"/{clientId}/{subClientId}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientConfig> updateClient(
            final @PathVariable String clientId,
            final @PathVariable String subClientId,
            final @Valid @RequestBody ClientUpdateRequest clientRequest) {
        clientService.updateClient(clientId, subClientId, clientRequest);
        final var result = clientConfigService.getClientConfig(clientId, subClientId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Get clients", description = "Gets a list of all clients.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = SUCCESSFULLY_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "No clients found"),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClients() {
        final List<ClientConfig> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    private ResponseEntity<ConfigurationResponse> getClientConfigResponse(final String clientId, final String subClientId) {
        final ClientConfig result = clientConfigService.getClientConfig(clientId, subClientId);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ConfigurationResponse(result), HttpStatus.OK);
    }
}
