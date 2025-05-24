package com.chris.shopping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Authentication response containing JWT and basic user info")
public record AuthenticationDTO(@Schema(description = "returned jwt token") String token,
                                @Schema(example = "John", description = "user's first name") @NotBlank String firstName,
                                @Schema(example = "Doe", description = "user's last name") @NotBlank String lastName) {
}
