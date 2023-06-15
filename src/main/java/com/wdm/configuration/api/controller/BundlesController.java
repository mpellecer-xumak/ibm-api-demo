package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Attribute;
import com.wdm.configuration.api.model.Bundle;
import com.wdm.configuration.api.response.AllBundlesResponse;
import com.wdm.configuration.api.response.AttributeResponse;
import com.wdm.configuration.api.response.BundleResponse;
import com.wdm.configuration.api.response.BundlesResponse;
import com.wdm.configuration.api.service.BundlesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/config/bundles")
@Tag(name = "Bundles API", description = "The Bundles API documentation")
public class BundlesController {
    private static final String BUNDLE_NAME_PARAM_LABEL = "bundleName";
    private static final String BUNDLES_NOT_FOUND = "Bundles not found.";

    private final BundlesService bundlesService;

    @Operation(summary = "Retrieves all information regarding a specific bundle.",
            description = "Retrieves bundle information",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS,
                            description = "Successfully retrieved bundle information."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = BUNDLES_NOT_FOUND),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/bundle/{bundleName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBundle(@PathVariable final String bundleName) {
        final Bundle bundle = bundlesService.getBundle(bundleName);

        return new ResponseEntity<>(bundle, HttpStatus.OK);
    }

    @Operation(summary = "Retrieves all bundles even if they are not associated to any client.",
            description = "Retrieves all bundles.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS,
                            description = "Successfully retrieved list of bundles."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = BUNDLES_NOT_FOUND),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBundles() {
        final List<Bundle> bundleList = bundlesService.getBundleNames();

        AllBundlesResponse allBundlesResponse = new AllBundlesResponse();
        allBundlesResponse.setBundleList(bundleList);

        return new ResponseEntity<>(allBundlesResponse, HttpStatus.OK);
    }

    @Operation(summary = "Gets the bundles available for a client.",
            description = "Gets client bundles.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS,
                            description = "Successfully retrieved list of bundles."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = BUNDLES_NOT_FOUND),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBundles(
            @PathVariable final String clientId,
            @RequestParam(name = BUNDLE_NAME_PARAM_LABEL, required = false) String bundleName) {
        return getBundles(clientId, null, bundleName);
    }

    @Operation(summary = "Gets the bundles available for a client.",
            description = "Gets client bundles.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS,
                            description = "Successfully retrieved list of bundles."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = BUNDLES_NOT_FOUND),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = {"/{clientId}/{subClientId}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBundles(
            @PathVariable final String clientId,
            @PathVariable(required = false) final String subClientId,
            @RequestParam(name = BUNDLE_NAME_PARAM_LABEL, required = false) String bundleName) {

        if (StringUtils.isEmpty(bundleName)) {
            final Set<Bundle> bundles = bundlesService.getBundles(clientId, subClientId);
            return getBundlesResponse(bundles);
        }

        final Bundle bundle = bundlesService.getBundle(clientId, subClientId, bundleName);
        return getBundleResponse(bundle);
    }

    @Operation(summary = "Remove a bundle.",
            description = "Remove a bundle from the platform level.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully removed."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Bundle not found."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_MESSAGE,
                            description = "Bundle cannot be deleted due to a conflict with the data " +
                                    "(bundle has attributes or is associated with a client)."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{bundleName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removePlatformBundle(
            @PathVariable final String bundleName) {
        bundlesService.deleteBundle(bundleName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Remove an attribute.",
            description = "Remove an attribute from a bundle.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully removed."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Attribute not found."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{bundleName}/attributes/{attributeName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeAttributeBundle(
            @PathVariable final String bundleName,
            @PathVariable final String attributeName) {
        final Bundle bundle = bundlesService.removeAttributeBundle(bundleName, attributeName);
        return getBundleResponse(bundle);
    }

    @Operation(summary = "Create bundle",
            description = "Creates a bundle with its attributes",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Bundle was created."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS,
                            description = "The request payload is invalid."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS,
                            description = "Bundle cannot be created due to a conflict with the data (bundle already exists)."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS,
                            description = "Problem handling the request.")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bundle> createBundle(final @RequestBody @Valid Bundle bundle) {
        final Bundle result = bundlesService.createBundle(bundle);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Update bundle",
            description = "Updates a bundle with its attributes",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Bundle was updated."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, 
                            description = "The request payload is invalid."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS,
                            description = "Bundle not found"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS,
                            description = "Problem handling the request.")})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bundle> updateBundle(final @RequestBody @Valid Bundle bundle) {
        final Bundle result = bundlesService.updateBundle(bundle);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private ResponseEntity<BundlesResponse> getBundlesResponse(final Set<Bundle> bundles) {
        if (CollectionUtils.isEmpty(bundles)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new BundlesResponse(bundles), HttpStatus.OK);
    }

    private ResponseEntity<BundleResponse> getBundleResponse(final Bundle bundle) {
        if (bundle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final Set<AttributeResponse> attributes = bundle
                .getAttributes()
                .stream()
                .map(Attribute::getName)
                .map(AttributeResponse::new)
                .collect(Collectors.toSet());
        final var body = new BundleResponse(bundle.getName(), attributes);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
