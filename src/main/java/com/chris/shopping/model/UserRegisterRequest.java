package com.chris.shopping.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User email, password, first name and last name for registration")
public record UserRegisterRequest(@Email @Schema(example = "john.doe@example.com", description = "email in valid format") String email,
                                  @Size(min = 8) @Schema(example = "Password123", description = "password with at least 8 characters") String password,
                                  @NotBlank @Schema(example = "John", description = "user's first name") String firstName,
                                  @NotBlank @Schema(example = "Doe", description = "user's last name") String lastName) {
}
