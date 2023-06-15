package com.wdm.configuration.api.response;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttributeResponse {
    private String attributeName;

    public static ResponseEntity<AttributeResponse> build(Consumer<AttributeResponse> block, HttpStatus status) {
        final AttributeResponse response = new AttributeResponse();
        block.accept(response);
        return new ResponseEntity<>(response, status);
    }
}
