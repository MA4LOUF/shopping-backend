package com.chris.shopping.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response object")
public record ErrorResponseDTO(
        @Schema(description = "Error message explaining the problem", example = "error message")
        String error
) {}
