package com.wdm.configuration.api.controller;


import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.response.ClientAppendsResponse;
import com.wdm.configuration.api.service.ClientAppendService;
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
@Tag(name = "Client Appends Configuration", description = "The Client Appends Configuration API documentation")
public class ClientAppendsController {

    private final ClientAppendService clientAppendService;

    @Operation(summary = "Associate append to a client", description = "Associates existing append to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Append was associated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {"/{clientId}/appends"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Append> associateAppend(
            final @Valid @RequestBody Append append,
            @PathVariable final String clientId) {
        return this.associateAppend(append, clientId, null);
    }

    @Operation(summary = "Associate append to a client", description = "Associates existing append to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Append was associated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {"/{clientId}/{subClientId}/appends"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Append> associateAppend(
            final @Valid @RequestBody Append append,
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final Append result = clientAppendService.associateAppend(clientId, subClientId, append);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Get appends associated with a client", description = "Gets Appends for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved configuration data"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}/appends"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAppendsResponse> getAppendsIdentityResolutionConfiguration(
            @PathVariable final String clientId) {
        return this.getAppendsIdentityResolutionConfiguration(clientId, null);
    }

    @Operation(summary = "Get appends associated with a client", description = "Gets Appends for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved configuration data"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}/{subClientId}/appends"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAppendsResponse> getAppendsIdentityResolutionConfiguration(
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final List<Append> appends = clientAppendService.getAppendsClientConfig(clientId, subClientId);
        final ClientAppendsResponse response = ClientAppendsResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .appends(appends)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update an append assigned to a client",
            description = "Replaces the configuration for an append associated to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The append was updated correctly"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {"/{clientId}/appends"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAppendsResponse> updateAssociatedAppend(
            final @Valid @RequestBody Append append,
            @PathVariable final String clientId) {
        return this.updateAssociatedAppend(append, clientId, null);
    }

    @Operation(summary = "Updates an append assigned to a client",
            description = "Replaces the configuration for an append associated to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The append was updated correctly"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {"/{clientId}/{subClientId}/appends"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAppendsResponse> updateAssociatedAppend(
            final @Valid @RequestBody Append append,
            @PathVariable final String clientId,
            @PathVariable final String subClientId) {
        List<Append> appends = clientAppendService.updateAssociatedAppend(clientId, subClientId, append);
        final ClientAppendsResponse response = ClientAppendsResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .appends(appends)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Remove a client's append.",
            description = "Remove an append from a client.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully removed."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{clientId}/appends/{appendName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAppendsResponse> removeClientsAppend(
            @PathVariable final String clientId,
            @PathVariable final String appendName) {
        return this.removeClientsAppend(clientId, null, appendName);
    }

    @Operation(summary = "Remove a client's append.",
            description = "Remove an append from a client.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully removed."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{clientId}/{subClientId}/appends/{appendName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientAppendsResponse> removeClientsAppend(
            @PathVariable final String clientId,
            @PathVariable final String subClientId,
            @PathVariable final String appendName) {
        List<Append> appends = clientAppendService.removeClientsAppend(clientId, subClientId, appendName);
        final ClientAppendsResponse response = ClientAppendsResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .appends(appends)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
