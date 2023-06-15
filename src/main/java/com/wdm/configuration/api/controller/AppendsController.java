package com.wdm.configuration.api.controller;

import com.wdm.configuration.api.model.Append;
import com.wdm.configuration.api.response.AppendsResponse;
import com.wdm.configuration.api.service.AppendService;
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
@RequestMapping("/config/appends")
@Tag(name = "Appends API", description = "The Appends API documentation")
public class AppendsController {

    private final AppendService appendService;

    @Operation(summary = "Create append", description = "Creates an append if it does not exist",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_201_STATUS, description = "Append was created."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_400_STATUS, description = HttpCodes.HTTP_400_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Append> createAppend(@Valid @RequestBody Append append) {
        final Append result = appendService.createAppend(append);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Deletes an append.",
            description = "Deletes an append.",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Successfully deleted append."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "Append not found."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @DeleteMapping(value = "/{appendName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeAppend(@PathVariable final String appendName) {
        appendService.deleteAppend(appendName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update append", description = "Updates an append if it exists",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_201_STATUS, description = "Append was updated."),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = HttpCodes.HTTP_404_STATUS),
                    @ApiResponse(responseCode = HttpCodes.HTTP_409_STATUS, description = HttpCodes.HTTP_409_MESSAGE),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Append> updateAppend(@Valid @RequestBody Append append) {
        final Append result = appendService.updateAppend(append);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieves all the appends",
            description = "Retrieves all the existing appends",
            responses = {
                    @ApiResponse(responseCode = HttpCodes.HTTP_200_STATUS, description = "Appends retrieved successfully"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_404_STATUS, description = "The appends were not found"),
                    @ApiResponse(responseCode = HttpCodes.HTTP_500_STATUS, description = HttpCodes.HTTP_500_MESSAGE)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppendsResponse> getAppends() {
        final List<Append> appends = appendService.getAppends();
        final AppendsResponse response = new AppendsResponse(appends);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
