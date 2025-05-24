package com.chris.shopping.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "User email and password for login")
public record UserLoginRequest(@Schema(example = "john.doe@example.com", description = "email in valid format") @Email String email,
                               @Schema(example = "Password123", description = "password with size of at least 8 characters") @Size(min = 8) String password) {
}
