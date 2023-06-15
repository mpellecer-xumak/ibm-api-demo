package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.request.IdentityRequest;
import com.wdm.configuration.api.service.IdentityResolutionService;
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
@RequestMapping("/config/platforms/identity-resolution")
@Tag(name = "Identity resolution API", description = "The Beta Identity Resolution API documentation")
public class IdentityResolutionController {

    private final IdentityResolutionService identityResolutionService;

    @Operation(summary = "Gets default configuration for Identity Resolution",
    description = "Returns the default configuration for IR",
    responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved Identity Resolution default configuration."),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Configuration not found."),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = "Problem handling the request.")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IdentityRequest>> getIdentityResolutionConfiguration(){
        final var result = identityResolutionService.getIdentityResolutionConfiguration();
        if (result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @Operation(summary = "Modifies the default configuration for Identity Resolution",
    description = "Modifies received fields for identity resolution",
    responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully retrieved Identity Resolution default configuration."),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Configuration not found."),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = "Problem handling the request.")})
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityRequest> putIdentityResolutionConfiguration(
            final @Valid @RequestBody IdentityRequest config) {
        final var result = identityResolutionService.putProvidedConfiguration(config);
        if (result == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
