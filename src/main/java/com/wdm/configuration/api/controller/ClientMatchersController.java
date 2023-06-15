package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.model.MatcherHierarchyGroup;
import com.wdm.configuration.api.request.ClientMachersDeleteRequest;
import com.wdm.configuration.api.response.ClientMachersResponse;
import com.wdm.configuration.api.service.ClientMatcherService;
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
@Tag(name = "Client Matcher Configuration", description = "The Client Matcher Configuration API documentation")
public class ClientMatchersController {

    private final static String PATH_CLIENT = "/{clientId}/matchers";
    private final static String PATH_CLIENT_SUBCLIENT = "/{clientId}/{subClientId}/matchers";

    private final ClientMatcherService clientMatcherService;

    @Operation(summary = "Associate matcher to a client", description = "Associates existing matchers to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Matcher was associated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "The client doesn't exist"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {PATH_CLIENT},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matcher> associateMatcher(
            final @Valid @RequestBody Matcher matcher,
            @PathVariable final String clientId) {
        return this.associateMatcher(matcher, clientId, null);
    }

    @Operation(summary = "Associate matcher to a client", description = "Associates existing matchers to a client",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Matcher was associated"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "The client doesn't exist"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = { PATH_CLIENT_SUBCLIENT},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matcher> associateMatcher(
            final @Valid @RequestBody Matcher matcher,
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final Matcher result = clientMatcherService.associateMatcher(clientId, subClientId, matcher);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Get matchers associated with a client", description = "Gets Machers for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved configuration data"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {PATH_CLIENT}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientMachersResponse> getMatchersIdentityResolutionConfiguration(
            @PathVariable final String clientId) {
        return this.getMatchersIdentityResolutionConfiguration(clientId,null);
    }

    @Operation(summary = "Get matchers associated with a client", description = "Gets Machers for Identity Resolution configuration.", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved configuration data"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {PATH_CLIENT_SUBCLIENT}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientMachersResponse> getMatchersIdentityResolutionConfiguration(
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final List<MatcherHierarchyGroup> matchers = clientMatcherService.getMachersClientConfig(clientId, subClientId);
        final ClientMachersResponse response = ClientMachersResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .matchers(matchers)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update client matcher", description = "Updates the configuration of a client matcher", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully updated the client matcher"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {PATH_CLIENT}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matcher> updateClientMatcher(
            @PathVariable final String clientId,
            @Valid @RequestBody final Matcher matcher) {
        return this.updateClientMatcher(clientId, null, matcher);
    }

    @Operation(summary = "Update client matcher", description = "Updates the configuration of a client matcher", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully updated the client matcher"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = {PATH_CLIENT_SUBCLIENT}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matcher> updateClientMatcher(
            @PathVariable final String clientId,
            @PathVariable final String subClientId,
            @Valid @RequestBody final Matcher matcher) {
        final Matcher result = clientMatcherService.updateMatcher(clientId, subClientId, matcher);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @Operation(summary = "Delete client matcher", description = "Delete the configuration of a client matcher", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully delete the client matcher"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {PATH_CLIENT}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientMachersResponse> deleteClientMatcher(
            @PathVariable final String clientId,
            @Valid @RequestBody final ClientMachersDeleteRequest deleteRequest) {
        return this.deleteClientMatcher(clientId, null, deleteRequest);
    }

    @Operation(summary = "Delete client matcher", description = "Delete the configuration of a client matcher", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully delete the client matcher"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {PATH_CLIENT_SUBCLIENT}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientMachersResponse> deleteClientMatcher(
            @PathVariable final String clientId,
            @PathVariable final String subClientId,
            @Valid @RequestBody final ClientMachersDeleteRequest deleteRequest) {
        List<MatcherHierarchyGroup> matcherList = clientMatcherService.deleteClientMatcher(clientId, subClientId, deleteRequest);
        final ClientMachersResponse response = ClientMachersResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .matchers(matcherList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
