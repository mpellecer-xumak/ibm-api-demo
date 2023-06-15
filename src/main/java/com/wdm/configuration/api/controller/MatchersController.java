package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.SimpleName;
import com.wdm.configuration.api.request.MatchersCreationRequest;
import com.wdm.configuration.api.response.GenericListResponse;
import com.wdm.configuration.api.service.MatcherService;
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
@RequestMapping("/config/matchers")
@Tag(name = "Matchers API", description = "The Matchers API documentation")
public class MatchersController {

    private final MatcherService matcherService;

    @Operation(summary = "List matchers", description = "Returns the list of all existing matchers", responses = {
        @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "List of matchers retrieved"),
        @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = "There was an error getting the matchers")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericListResponse<SimpleName>> getMatchers() {
        final List<SimpleName> matchers = matcherService.getExistingMatchers();
        final GenericListResponse<SimpleName> response = GenericListResponse.<SimpleName>builder().response(matchers).build();
        return new ResponseEntity<GenericListResponse<SimpleName>>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create matchers", description = "Creates a list of matchers", responses = {
        @ApiResponse(responseCode = HttpCodes.HTTP_201_STATUS, description = "The matchers were created"),
        @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = "The request body is not correct"),
        @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = "Some matchers in the request already exist"),
        @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = "There was an error getting the matchers")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimpleName>> createMatchers(
            final @Valid @RequestBody MatchersCreationRequest matcherRequest) {
        final List<SimpleName> result = matcherService.createMatchers(matcherRequest.getMatchers());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Remove a matcher.",
            description = "Remove a matcher from the platform level.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully removed."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Matcher not found."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_MESSAGE,
                            description = "Matcher cannot be deleted due to a conflict with the data " +
                                    "(Matcher is associated with a client or a platform)."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = {"/{matcherName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removePlatformBundle(
            @PathVariable final String matcherName) {
        matcherService.deleteMatcher(matcherName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
