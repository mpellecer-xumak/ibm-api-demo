package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.VersionManagement;
import com.wdm.configuration.api.response.DeploymentStateResponse;
import com.wdm.configuration.api.response.DeploymentsResponse;
import com.wdm.configuration.api.service.VersionManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/config/deployments")
@Tag(name = "Deployments API", description = "The Deployments API documentation")
public class DeploymentsController {

    public static final String BUILD_NAME = "build";
    private final VersionManagementService versionManagementService;

    @Operation(summary = "Versions management list",
            description = "Gets the list of versions managements with its state",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The list of versions management was successfully retrieved"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeploymentsResponse> getDeployments() {
        final List<VersionManagement> results = versionManagementService.getDeploymentVersions();
        if (results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new DeploymentsResponse(results), HttpStatus.OK);
    }

    @Operation(summary = "State of a specify version management",
            description = "Gets a version management state filtered by entity.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The entity was found and the state returned"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Entity state not found"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = "/{entity}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeploymentStateResponse> getDeploymentState(@PathVariable final String entity) {
        final VersionManagement versionManagement = versionManagementService.getDeploymentVersion(entity);
        if (versionManagement == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new DeploymentStateResponse(versionManagement.getState().name(),null,null), HttpStatus.OK);
    }

    @Operation(summary = "Switches the deployment version",
            description = "Switches between the two deployment versions",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The entity was found and the new state returned"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Entity state not found"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = "/{entity}/switch",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VersionManagement> switchDeployment(@PathVariable final String entity, @RequestParam(name = BUILD_NAME, required = false) final String build){
        final VersionManagement newDeploymentVersion = versionManagementService.changeDeploymentVersion(entity, build);
        if (newDeploymentVersion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newDeploymentVersion, HttpStatus.OK);
    }
}
