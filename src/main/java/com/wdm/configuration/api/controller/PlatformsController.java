package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Matcher;
import com.wdm.configuration.api.model.PlatformMatcher;
import com.wdm.configuration.api.response.PlatformMatchersResponse;
import com.wdm.configuration.api.service.PlatformService;
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
@RequestMapping("/config/platforms")
@Tag(name = "Platform configuration API", description = "The Platform configuration API documentation")
public class PlatformsController {

    private final PlatformService platformService;

    @Operation(summary = "Create platform matchers",
            description = "Creates platform matchers for existing matchers and hierarchies", responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_201_STATUS, description = "The platform matcher was created successfully"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = "The platform matcher already exists"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = "Problem handling the request.")})
    @PostMapping(value = "/matchers", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matcher> createPlatformMatcher(final @Valid @RequestBody Matcher matcher) {
        final Matcher result = platformService.createPlatformMatcher(matcher);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Updates a platform matcher",
            description = "Updates a platform matcher with existing matcher and hierarchy", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "The platform matcher was updated successfully"),
            @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "The platform matcher was not found"),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(value = "/matchers", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matcher> updatePlatformMatcher(final @Valid @RequestBody Matcher matcher) {
        final Matcher result = platformService.updatePlatformMatcher(matcher);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "List all the platform matchers",
            description = "Return all the platform matchers and their settings", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "List of platform matchers retrieved"),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(value = "/matchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlatformMatchersResponse> getPlatformMatchers() {
        final List<PlatformMatcher> platformMatchers = platformService.getPlatformMatchers();
        final PlatformMatchersResponse response = PlatformMatchersResponse.builder()
                .matchers(platformMatchers)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a platform matchers",
            description = "Delete a matchers from the platform level", responses = {
            @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "List of platform matchers after delete"),
            @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = "/matchers/{matcherName}/{hierarchyName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlatformMatchersResponse> deletePlatformMatchers(
            @PathVariable final String matcherName,
            @PathVariable final String hierarchyName) {
        final List<PlatformMatcher> platformMatchers = platformService.deletePlatformMatchers(matcherName, hierarchyName);
        final PlatformMatchersResponse response = PlatformMatchersResponse.builder()
                .matchers(platformMatchers)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
