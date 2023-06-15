package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.request.BundlesRequest;
import com.wdm.configuration.api.response.ClientBundlesResponse;
import com.wdm.configuration.api.service.BundlesService;
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
@Tag(name = "Client Human Insights Bundles Configuration", description = "The Client Human Insights Bundles Configuration API documentation")
public class ClientBundlesController {

    private final BundlesService bundlesService;

    @Operation(summary = "Associates human insights bundles", description = "Associates human insights to a client", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Human insights were associated"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {"/{clientId}/bundles"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientBundlesResponse> associateHumanInsights(
            final @Valid @RequestBody BundlesRequest bundlesRequest,
            @PathVariable final String clientId) {
        return this.associateHumanInsights(bundlesRequest, clientId, null);
    }

    @Operation(summary = "Associates human insights bundles", description = "Associates human insights to a client", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Human insights were associated"),
            @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(value = {"/{clientId}/{subClientId}/bundles"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientBundlesResponse> associateHumanInsights(
            final @Valid @RequestBody BundlesRequest bundlesRequest,
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {
        final List<Bundle> bundles = bundlesService.assignHumanInsightBundlesToClient(
                clientId, subClientId, bundlesRequest.getBundles());
        final ClientBundlesResponse response = ClientBundlesResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .bundles(bundles)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove human insights bundles",
            description = "Remove human insights bundle from a client",
            responses = {
                @ApiResponse(
                        responseCode = HttpCodes.HTTP_200_STATUS,
                        description = "Human insights which were deleted"),
                @ApiResponse(
                        responseCode = HttpCodes.HTTP_404_STATUS,
                        description = HttpCodes.HTTP_404_MESSAGE),
                @ApiResponse(
                        responseCode = HttpCodes.HTTP_500_STATUS,
                        description = HttpCodes.HTTP_500_MESSAGE)
            })
    @DeleteMapping(value = {"/{clientId}/bundles"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientBundlesResponse> deleteClientBundle(
            final @Valid @RequestBody BundlesRequest bundlesRequest,
            @PathVariable final String clientId) {

        return this.deleteClientBundle(bundlesRequest, clientId, null);
    }

    @Operation(
            summary = "Remove human insights bundles",
            description = "Remove human insights bundle from a client/sub client",
            responses = {
                @ApiResponse(
                        responseCode = HttpCodes.HTTP_200_STATUS,
                        description = "Human insights which were deleted"),
                @ApiResponse(
                        responseCode = HttpCodes.HTTP_404_STATUS,
                        description = HttpCodes.HTTP_404_MESSAGE),
                @ApiResponse(
                        responseCode = HttpCodes.HTTP_500_STATUS,
                        description = HttpCodes.HTTP_500_MESSAGE)
            })
    @DeleteMapping(value = {"/{clientId}/{subClientId}/bundles"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientBundlesResponse> deleteClientBundle(
            final @Valid @RequestBody BundlesRequest bundlesRequest,
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId) {

        final List<Bundle> bundles = bundlesService.removeClientBundles(
                clientId,
                subClientId,
                bundlesRequest.getBundles()
        );
        final ClientBundlesResponse response = ClientBundlesResponse.builder()
                .clientId(clientId)
                .subClientId(subClientId)
                .bundles(bundles)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
